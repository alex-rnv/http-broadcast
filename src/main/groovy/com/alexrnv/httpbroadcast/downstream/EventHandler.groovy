package com.alexrnv.httpbroadcast.downstream

import io.vertx.groovy.core.http.HttpServerResponse

/**
 * Created: 8/21/15 7:21 PM
 * Author: alex
 */
abstract class EventHandler implements DownstreamEventListener {

    protected HttpServerResponse response
    protected int numDownstreams
    protected volatile boolean first = true

    EventHandler(HttpServerResponse response, int numDownstreams) {
        this.response = response
        this.numDownstreams = numDownstreams
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