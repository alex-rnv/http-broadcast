package com.alexrnv.httpbroadcast.downstream

import io.vertx.groovy.core.http.HttpServerResponse

/**
 * Created: 8/21/15 7:21 PM
 * Author: alex
 */
abstract class EventHandler implements DownstreamEventListener {

    static final int HTTP_CODE_OK = 200
    static final int HTTP_CODE_REDIRECT = 300
    static final int HTTP_CODE_ERR = 500

    protected HttpServerResponse response
    protected int numDownstreams
    protected volatile boolean first = true

    EventHandler(HttpServerResponse response, int numDownstreams) {
        this.response = response
        this.numDownstreams = numDownstreams
    }

    boolean isCodeOk(int code) {
        code >= HTTP_CODE_OK && code < HTTP_CODE_REDIRECT
    }

    void send(int status) {
        response.setStatusCode(status).end("Ok!")
    }

    synchronized void sendFirst(int status) {
        if(first) {
            first = false
            send(status)
        } else {

        }
    }
}