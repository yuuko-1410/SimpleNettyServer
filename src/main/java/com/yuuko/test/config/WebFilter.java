package com.yuuko.test.config;

import com.yuuko.SimpleServer.anno.Config;
import com.yuuko.SimpleServer.config.BaseFilterConfig;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;

@Config
public class WebFilter implements BaseFilterConfig {
    @Override
    public boolean filterConfig(FullHttpRequest request, DefaultFullHttpResponse response) {
        return false;
    }

    @Override
    public DefaultFullHttpResponse getFilterResponse(FullHttpRequest request, DefaultFullHttpResponse response) {
        return null;
    }
}
