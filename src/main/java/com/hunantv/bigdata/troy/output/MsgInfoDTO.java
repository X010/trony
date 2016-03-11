package com.hunantv.bigdata.troy.output;

/**
 * 封装日志信息，用于压入队列
 * author: xiaolong.yuanxl
 * date: 2015-05-27 下午4:06
 */
public class MsgInfoDTO {

    private String logFullPath;//日志全路径,细化到了 normal,error,debug

    private String content; //日志内容

    private String split; //切割策略

    public MsgInfoDTO(String logFullPath, String content, String split) {
        this.logFullPath = logFullPath;
        this.content = content;
        this.split = split;
    }

    public String getLogFullPath() {
        return logFullPath;
    }

    public String getContent() {
        return content;
    }

    public String getSplit() {
        return split;
    }
}
