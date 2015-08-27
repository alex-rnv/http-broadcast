package com.alexrnv.httpbroadcast.downstream

import io.vertx.groovy.core.http.HttpClientRequest
import io.vertx.groovy.core.http.HttpClientResponse
import io.vertx.groovy.core.http.HttpServerResponse
/**
 * Created: 8/24/15 4:40 AM
 * Author: alex
 */
class NoWaitEventHandler extends EventHandler {

    NoWaitEventHandler(HttpServerResponse response, int numDownstreams) {
        super(response, numDownstreams)
    }

    @Override
    void onDownstreamRequest(HttpClientRequest request) {
        sendFirst(HTTP_CODE_OK)
    }

    @Override
    void onDownstreamResponse(HttpClientResponse httpClientResponse) {
    }

}
