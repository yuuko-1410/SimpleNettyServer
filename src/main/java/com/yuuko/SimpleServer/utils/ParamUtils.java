package com.yuuko.SimpleServer.utils;

import com.google.gson.Gson;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.MemoryAttribute;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static io.netty.handler.codec.http.HttpMethod.GET;
import static io.netty.handler.codec.http.HttpMethod.POST;


public class ParamUtils {
    public static Map<String,String> getParams(FullHttpRequest req){
        Map<String, String>requestParams=new HashMap<>();
        // 处理get请求
        if (req.getMethod() == GET) {
            QueryStringDecoder decoder = new QueryStringDecoder(req.getUri());
            Map<String, List<String>> parame = decoder.parameters();
            Iterator<Map.Entry<String, List<String>>> iterator = parame.entrySet().iterator();
            while(iterator.hasNext()){
                Map.Entry<String, List<String>> next = iterator.next();
                requestParams.put(next.getKey(), next.getValue().get(0));
            }
        }
        // 处理POST请求
        if (req.getMethod() == POST) {
            HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(
                    new DefaultHttpDataFactory(false), req);
            List<InterfaceHttpData> postData = decoder.getBodyHttpDatas(); //
            for(InterfaceHttpData data:postData){
                if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.Attribute) {
                    MemoryAttribute attribute = (MemoryAttribute) data;
                    requestParams.put(attribute.getName(), attribute.getValue());
                }
            }
        }

        Gson gson = new Gson();
        String json = gson.toJson(requestParams);
        return requestParams;
    }
    public static Object getBeanToJson(FullHttpRequest request, Class<?> aClass){
        String json = request.content().toString(StandardCharsets.UTF_8);
        Gson gson = new Gson();
        return gson.fromJson(json, aClass);
    }
    public static Object getBeanToMap(Object obj, Class<?> aClass){
        Gson gson = new Gson();
        return gson.fromJson(gson.toJson(obj), aClass);
    }
}
