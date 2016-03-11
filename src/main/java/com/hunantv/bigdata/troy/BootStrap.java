package com.hunantv.bigdata.troy;

import com.hunantv.bigdata.troy.configure.AbstractConfigManager;
import com.hunantv.bigdata.troy.configure.LocalConfigManager;
import com.hunantv.bigdata.troy.configure.ZkConfigManager;
import com.hunantv.bigdata.troy.dispatcher.DispatcherChannelInitializer;
import com.hunantv.bigdata.troy.dispatcher.MonitorChannelInitializer;
import com.hunantv.bigdata.troy.statistics.StatisticsUtils;
import com.hunantv.bigdata.troy.tools.Constants;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;


/**
 * The mainClass to lanuch jar
 */
public class BootStrap {

    private final static Logger LOG = LoggerFactory.getLogger(BootStrap.class);


    public static void main(String[] args) {

        CommandLineParser parser = new BasicParser();
        Options options = new Options();
        options.addOption("h", "help", false, "Print help for this application");
        options.addOption("sc", "serverconfig", true, "server config file");
        options.addOption("lc", "logconfig", true, "log config file");

        CommandLine commandLine;
        try {
            commandLine = parser.parse(options, args);
        } catch (ParseException e) {
            LOG.error("command line parsed fail. input args : " + Arrays.asList(args), e);
            return;
        }

        if (commandLine.hasOption('h')) {
            HelpFormatter hf = new HelpFormatter();
            hf.printHelp("OptionsTip", options);
            return;
        }

        String scFile, lcFile;
        AbstractConfigManager configManager;
        if (commandLine.hasOption("sc")) {
            scFile = commandLine.getOptionValue("sc");
        } else {
            LOG.warn("no server config file option input");
            return;
        }

        if(commandLine.hasOption("lc")) {
            lcFile = commandLine.getOptionValue("lc");
            configManager = new LocalConfigManager(scFile, lcFile);
            LOG.info("using local log configuration. " + lcFile + " and server config: " + scFile);
        } else {
            configManager = new ZkConfigManager(scFile);
            LOG.info("using zookeeper log configuration. input server config: " + scFile);
        }

        try {
            configManager.load();
            //加载配置后立即一次性创建所有目录
            DirCreator.createDir(configManager);
            // init statistics module
            new StatisticsUtils(configManager);
        } catch (Exception e) {
            LOG.error("Exception caught when load conf file.", e);
            return;
        }

//        AsyncWriteMsgUtil awmUtil = new AsyncWriteMsgUtil(configManager);
//        awmUtil.start();

        //启动Server
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        LOG.info("start server,host:" + configManager.getHost() + ",port:" + configManager.getPort());
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        // ChannelOption.SO_BACKLOG: The maximum queue length for incoming connection indications (a request to connect) is set to the backlog parameter. If a connection indication arrives when the queue is full, the connection is refused.
        serverBootstrap.option(ChannelOption.SO_BACKLOG, 10240);
        serverBootstrap.option(ChannelOption.TCP_NODELAY, true);
        serverBootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).childHandler(new DispatcherChannelInitializer(configManager));

        //启动 Monitor Server
        EventLoopGroup monitorBossGroup = new NioEventLoopGroup();
        EventLoopGroup monitorWorkerGroup = new NioEventLoopGroup();

        LOG.info("start monitor server on port 36969.");
        ServerBootstrap monitorServerBootstrap = new ServerBootstrap();
        monitorServerBootstrap.option(ChannelOption.SO_BACKLOG, 1024);
        monitorServerBootstrap.group(monitorBossGroup, monitorWorkerGroup).channel(NioServerSocketChannel.class).childHandler(new MonitorChannelInitializer());

        try {
            Channel ch = serverBootstrap.bind(configManager.getHost(), configManager.getPort()).sync().channel();
            Channel monitorCh = monitorServerBootstrap.bind(Constants.MONITOR_HOST, Constants.MONITOR_PORT).sync().channel();

            ch.closeFuture().sync();
            monitorCh.closeFuture().sync();
        } catch (InterruptedException e) {
            LOG.error("Exception caught when channel sync.", e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            monitorBossGroup.shutdownGracefully();
            monitorWorkerGroup.shutdownGracefully();
        }
    }
}
