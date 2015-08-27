package com.alexrnv.httpbroadcast.configuration

import io.vertx.core.json.JsonObject

/**
 * Created: 8/25/15 4:21 PM
 * Author: alex
 */
class ResourceConfReader implements ConfReader {

    private final String resourceName

    ResourceConfReader(String resourceName) {
        this.resourceName = resourceName
    }

    @Override
    JsonObject getConfig() {
        return new JsonObject(this.class.getClassLoader().getResource(resourceName).text)
    }
}
