package com.alexrnv.httpbroadcast
import com.alexrnv.httpbroadcast.configuration.ConfReader
import com.alexrnv.httpbroadcast.configuration.FileConfReader
import com.alexrnv.httpbroadcast.upstream.HttpBroadcastServer
import groovy.util.logging.Log
import io.vertx.core.AsyncResult
import io.vertx.core.Handler
import io.vertx.core.json.JsonObject
import io.vertx.groovy.core.Vertx

import java.util.concurrent.CountDownLatch
/**
 * Helper class for creating verticles internally (debugging and testing).
 * Created: 8/21/15 11:29 AM
 * Author: alex
 */
@Log
class HttpBroadcast {

    private Vertx vertx
    private final ConfReader confReader
    private volatile String deploymentID

    HttpBroadcast(Vertx vertx, ConfReader confReader) {
        this.vertx = vertx
        this.confReader = confReader
    }

    HttpBroadcast startAsync() {
        start0(startHandler(null))
        this
    }

    HttpBroadcast startSync() {
        def latch = new CountDownLatch(1)
        start0(startHandler(latch))
        latch.await()
        this
    }

    void stopAsync() {
        stop0(stopHandler(null))
    }

    void stopSync() {
        def latch = new CountDownLatch(1)
        stop0(stopHandler(latch))
        latch.await()
    }

    private void start0(Handler<AsyncResult<String>> completionHandler) {
        JsonObject config = confReader.getConfig()

        def options = [
                "config" : config,
                "instances" : 1
        ];

        vertx.deployVerticle("groovy:" + HttpBroadcastServer.class.getName(), options, completionHandler)
    }

    private void stop0(Handler<AsyncResult<String>> completionHandler) {
        if(deploymentID != null) {
            vertx.undeploy(deploymentID, completionHandler)
            deploymentID = null
        }
    }

    private def startHandler0 = { CountDownLatch latch, AsyncResult<String> r ->
        deploymentID = r.result()
        if(r.succeeded()) {
            log.info "Upstream server started"
        } else {
            log.error "Upstream server start failed", r.cause()
        }
        if(latch != null)
            latch.countDown()
    }
    private def startHandler = { CountDownLatch latch -> startHandler0.curry(latch) }

    private def stopHandler0 = { CountDownLatch latch, AsyncResult<String> r ->
        if(r.succeeded()) {
            log.info "Upstream server stopped"
        } else {
            log.error "Upstream server was not stopped correctly", r.cause()
        }

        if(latch != null)
            latch.countDown()
    }
    private def stopHandler = { CountDownLatch latch -> stopHandler0.curry(latch) }

    public static void main(String[] args) {
        new HttpBroadcast(Vertx.vertx(), new FileConfReader(args[0])).startAsync()
    }

}
