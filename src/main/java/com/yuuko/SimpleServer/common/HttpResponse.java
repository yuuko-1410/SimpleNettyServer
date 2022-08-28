package com.yuuko.SimpleServer.common;

import io.netty.handler.codec.http.FullHttpResponse;

public class HttpResponse {
    private final FullHttpResponse fullHttpResponse;

    public HttpResponse(FullHttpResponse fullHttpResponse) {
        this.fullHttpResponse = fullHttpResponse;
    }
}
