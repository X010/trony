package com.hunantv.bigdata.troy.tools;

/**
 * Created by wuxinyong on 15-1-21.
 */
public interface Constants {

    public static final String DEFAULT_PATH = "default";
    public static final String STATUS_DEBUG = "debug";
    public static final String STATUS_ERROR = "error";

    public static final String DEFAULT_SPLIT = "day";



    public static final String CONFIG_HOST = "host";
    public static final String CONFIG_PORT = "port";
    public static final String CONFIG_COMM_ERROR_PATH = "comm_error_path";
    public static final String CONFIG_STAT_PATH = "stat_path";

    public static final String HTTP_HEADER_HOST = "Host";
    public static final String HTTP_HEADER_UA = "User-Agent";
    public static final String HTTP_HEADER_ACCEPT = "Accept";
    public static final String HTTP_HEADER_ACCEPT_LANGUAGE = "Accept-Encoding";
    public static final String HTTP_HEADER_ACCEPT_ENCODING = "Accept-Language";
    public static final String HTTP_HEADER_COOKIE = "Cookie";
    public static final String HTTP_HEADER_CONNECTION = "Connection";
    public static final String HTTP_HEADER_CONTENT_TYPE = "Content-Type";
    public static final String HTTP_HEADER_REFERER = "Referer";
    public static final String HTTP_HEADER_BATCH = "Bg-Batch";
    public static final String HTTP_HEADER_BATCH_VALUE = "true";

    public static final String KEY_COMMON = "common";
    public static final String KEY_EVENTS = "events";

    public static final String CONTENT_TYPE = "Content-Type";

    public static final String KEY_BID = "bid";
    public static final String KEY_ACT = "act";
    public static final String KEY_IS_DEBUG = "isdebug";
    public static final String VALUE_IS_DEBUG = "1";

    public static final String INVALID_BID = "INVALID_BID";

    public static final String TOTAL_BID = "TOTAL";

    public static final String DEFAULT_FLOW = "default";

    public static final String VALIDATE_SUCC_FLAG = "0";
    public static final String VALIDATE_ERROR_FLAG = "1";

    public static final String FORMAT_SPLIT = "\t";

    public static final String MONITOR_HOST = "0.0.0.0";
    public static final int MONITOR_PORT = 36969;


    public static final String LOG_QUEUE_SIZE = "log_queue_size";
    public static final String LOG_QUEUE_NUM = "log_queue_num";
    public static final String LOG_THREAD_PERIOD = "log_thread_period";
    public static final String LOG_THREAD_CONSUMER_BATCH_SIZE = "log_thread_consumer_batchSize";

    public static final String ZK_ADDRESS = "ZK_ADDRESS";
    public static final String ZK_TIMEOUT = "ZK_TIMEOUT";
    public static final String ZK_CONFIG_ROOTNODE = "ZK_CONFIG_ROOTNODE";
    public static final String ZK_CONF_ENCODING = "ZK_CONF_ENCODING";
}
