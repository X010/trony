package com.hunantv.bigdata.troy.tools;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;

import java.util.Date;

/**
 * Created by wuxinyong on 15-1-21.
 */
public class JsonUtils {
    /**
     * 最简单序列化为Json数据
     *
     * @param ojbObject
     * @return
     */
    public static String simpleJson(Object ojbObject) {
        SerializeConfig sc = new SerializeConfig();
        sc.put(Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm"));
        return JSON.toJSONString(ojbObject, sc);
    }

    /**
     * 返序列化对象
     *
     * @param content
     * @return
     */
    public static Object StringToJsonVideo(String content, Class classZ) {
        return JSON.parseObject(content, classZ);
    }

    public static Object StringToJsonArray(String content) {
        return JSON.parse(content);
    }
}
