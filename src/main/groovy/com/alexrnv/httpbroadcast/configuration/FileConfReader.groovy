package com.alexrnv.httpbroadcast.configuration

import io.vertx.core.json.JsonObject

/**
 * Created: 8/28/15 12:13 PM
 * Author: alex
 */
class FileConfReader implements ConfReader {

    String fileName

    FileConfReader(String fileName) {
        this.fileName = fileName
    }

    @Override
    JsonObject getConfig() {
        return new JsonObject(new File(fileName).text)
    }
}
