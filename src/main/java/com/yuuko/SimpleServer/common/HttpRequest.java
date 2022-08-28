package com.yuuko.SimpleServer.common;

import com.yuuko.SimpleServer.utils.ParamUtils;
import com.yuuko.SimpleServer.utils.UrlUtils;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import java.util.Map;
public class HttpRequest{
    private final FullHttpRequest fullHttpRequest;
    public FullHttpRequest getFullHttpRequest(){
        return fullHttpRequest;
    }
    public HttpRequest(FullHttpRequest fullHttpRequest) {
        this.fullHttpRequest = fullHttpRequest;
    }
    public String baseUrl(){
        return UrlUtils.getBaseUrl(fullHttpRequest.uri());
    }
    public Map<String,String> urlPrams(){
        return UrlUtils.getUrlPrams(fullHttpRequest.uri());
    }
    public Map<String, String> getParams(){
        return ParamUtils.getParams(fullHttpRequest);
    }
    public HttpHeaders header(){
        return fullHttpRequest.headers();
    }


}
