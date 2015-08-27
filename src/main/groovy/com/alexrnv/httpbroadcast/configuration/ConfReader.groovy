package com.alexrnv.httpbroadcast.configuration

import io.vertx.core.json.JsonObject

/**
 * Created: 8/25/15 4:24 PM
 * Author: alex
 */
interface ConfReader {
    JsonObject getConfig()
}