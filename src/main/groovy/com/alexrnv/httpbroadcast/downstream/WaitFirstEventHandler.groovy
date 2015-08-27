package com.alexrnv.httpbroadcast.downstream
import io.vertx.groovy.core.http.HttpClientRequest
import io.vertx.groovy.core.http.HttpClientResponse
import io.vertx.groovy.core.http.HttpServerResponse

import java.util.concurrent.atomic.AtomicInteger

/**
 * Created: 8/24/15 12:07 PM
 * Author: alex
 */
class WaitFirstEventHandler extends EventHandler {

    private AtomicInteger numResponses = new AtomicInteger()

    WaitFirstEventHandler(HttpServerResponse response, int numDownstreams) {
        super(response, numDownstreams)
    }

    @Override
    void onDownstreamRequest(HttpClientRequest request) {
    }

    @Override
    synchronized void onDownstreamResponse(HttpClientResponse httpClientResponse) {
        int code = httpClientResponse.statusCode()
        if(isCodeOk(code)) {
            sendFirst(code)
        } else if (numResponses.incrementAndGet() >= numDownstreams) {
            sendFirst(HTTP_CODE_ERR)
        }
    }
}
