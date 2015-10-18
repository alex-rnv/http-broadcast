package com.alexrnv.httpbroadcast.downstream

import io.vertx.groovy.core.http.HttpClientRequest
import io.vertx.groovy.core.http.HttpClientResponse

/**
 * Created: 8/24/15 11:45 AM
 * Author: alex
 */
interface DownstreamEventListener {
    /**
     * Fires when httpClientRequest comes to downstream handler from upstream handler.
     * @param httpClientRequest - upstream httpClientRequest (updated version of client httpClientRequest)
     */
    void onDownstreamRequest(HttpClientRequest httpClientRequest)

    /**
     * Fires when response comes from downstream system.
     * @param httpClientResponse - response from external downstream system
     */
    void onDownstreamResponse(HttpClientResponse httpClientResponse)
}