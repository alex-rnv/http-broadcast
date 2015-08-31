package com.alexrnv.httpbroadcast.downstream

import groovy.util.logging.Log
import io.vertx.groovy.core.http.HttpClientRequest
import io.vertx.groovy.core.http.HttpClientResponse
import io.vertx.groovy.core.http.HttpServerResponse
/**
 * Created: 8/24/15 4:40 AM
 * Author: alex
 */
@Log
class NoWaitEventHandler extends EventHandler {

    NoWaitEventHandler(HttpServerResponse response, int numDownstreams) {
        super(response, numDownstreams)
    }

    @Override
    void onDownstreamRequest(HttpClientRequest request) {
        sendFirst(HTTP_CODE_OK)
    }

    @Override
    void onDownstreamResponse(HttpClientResponse r) {
        def status = r.statusCode()
        log.info "Received and ignored response, status $status"
    }

}
