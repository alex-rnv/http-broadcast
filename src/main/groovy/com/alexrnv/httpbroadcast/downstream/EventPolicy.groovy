package com.alexrnv.httpbroadcast.downstream

import io.vertx.groovy.core.http.HttpServerResponse


/**
 * Created: 8/21/15 7:20 PM
 * Author: alex
 */
class EventPolicy {

    private static def handlersMap = [
            NO_WAIT : NoWaitEventHandler.class,
            WAIT_FIRST : WaitFirstEventHandler.class
    ]

    static EventPolicy create(String name) {
        new EventPolicy(name)
    }

    private HttpServerResponse httpServerResponse
    private int numDownstreams
    private Class handler

    private EventPolicy(String name) {
        this.handler = handlersMap[name]
        if(this.handler == null) {
            throw new IllegalArgumentException('Wrong name $name, expected one of $handlersMap.keySet()')
        }
    }

    EventPolicy withServerResponse(HttpServerResponse httpServerResponse) {
        this.httpServerResponse = httpServerResponse
        this
    }

    EventPolicy withDownstreams(int numDownstreams) {
        this.numDownstreams = numDownstreams
        this
    }

    EventHandler handler() {
        if(this.numDownstreams == 0) {
            throw new IllegalArgumentException('Number of downstreams is not set')
        }
        if(this.httpServerResponse == null) {
            throw new IllegalArgumentException('Server response instance is not set')
        }
        this.handler.newInstance(httpServerResponse, numDownstreams)
    }
}