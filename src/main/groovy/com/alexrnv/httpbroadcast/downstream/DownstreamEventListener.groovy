package com.alexrnv.httpbroadcast.downstream

import io.vertx.groovy.core.http.HttpClientRequest
import io.vertx.groovy.core.http.HttpClientResponse

/**
 * Created: 8/24/15 11:45 AM
 * Author: alex
 */
interface DownstreamEventListener {
    void onDownstreamRequest(HttpClientRequest request)
    void onDownstreamResponse(HttpClientResponse httpClientResponse)
}