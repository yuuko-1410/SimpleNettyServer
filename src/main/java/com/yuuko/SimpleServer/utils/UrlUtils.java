package com.yuuko.SimpleServer.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlUtils {
    public static String getBaseUrl(String url){
        int p = !url.contains("?") ? url.length() : url.indexOf("?");
        return url.substring(0,p);
    }
    //add?name=123&pojo=222
    public static Map<String,String> getUrlPrams(String url){
        Map<String,String> map = new HashMap<>();
        String pattern = "[?&]([^?&#]+)=([^?&#]+)";
        // 创建 Pattern 对象
        Pattern r = Pattern.compile(pattern);
        // 现在创建 matcher 对象
        Matcher m = r.matcher(url);
        while (m.find()){
            String s = m.group().replaceAll("\\?", "").replaceAll("&", "");
            System.out.println(s);
            int temp = !s.contains("=") ? s.length() : s.indexOf("=");
            map.put(s.substring(0,temp),s.substring(temp+1,s.length()));
        }
        return map;
    }
}
