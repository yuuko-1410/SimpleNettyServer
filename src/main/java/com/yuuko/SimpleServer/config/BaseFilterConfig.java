package com.yuuko.SimpleServer.config;


import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;

public interface BaseFilterConfig {
    boolean filterConfig(FullHttpRequest request, DefaultFullHttpResponse response);
    DefaultFullHttpResponse getFilterResponse(FullHttpRequest request, DefaultFullHttpResponse response);
}
