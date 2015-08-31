package com.alexrnv.httpbroadcast
import com.alexrnv.httpbroadcast.configuration.ResourceConfReader
import io.vertx.groovy.core.Vertx
import io.vertx.groovy.core.http.HttpClient
import io.vertx.groovy.core.http.HttpServer
import io.vertx.groovy.ext.unit.Async
import io.vertx.groovy.ext.unit.TestContext
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
/**
 * Created: 8/24/15 12:44 PM
 * Author: alex
 */
@RunWith(io.vertx.groovy.ext.unit.junit.VertxUnitRunner)
class HttpBroadcastTest extends GroovyTestCase {

    Vertx vertx
    HttpServer[] servers
    HttpClient client
    HttpBroadcast broadcast

    @Before
     void before(TestContext context) {
        vertx = Vertx.vertx()
        client = vertx.createHttpClient()
        servers = [
                createServer(vertx, context, 8880, { req -> req.response().setStatusCode(200).end() }),
                createServer(vertx, context, 8881, { req -> req.response().setStatusCode(500).end() }),
                createServer(vertx, context, 8882, { req -> }),
                createServer(vertx, context, 8883, { req ->
                    int code
                    switch (req.uri()) {
                        case "/300":
                            code = 300
                            break
                        case "/500":
                            code = 500
                            break
                        case "/200":
                        default:
                            code = 200
                            break
                    }
                    req.response().setStatusCode(code).end()
                })
        ]
    }

    @After
    void after(TestContext context) {
        broadcast.stopSync()
        client.close()
        servers.each { srv -> srv.close(assertSuccess) }
        vertx.close(assertSuccess)
    }

    @Test
    void NoWait_1Downstream_1Response_Expect200(TestContext context) {
        broadcast = startNewBroadcast("conf-1downstream-1response-nowait.json")
        postAndExpect200(context)
    }

    @Test
    void NoWait_3Downstreams_1Response_Expect200(TestContext context) {
        broadcast = startNewBroadcast("conf-3downstreams-1response-nowait.json")
        postAndExpect200(context)
    }

    @Test
    void NoWait_1Downstream_FailResponse_Expect200(TestContext context) {
        broadcast = startNewBroadcast("conf-1downstream-failresponse-nowait.json")
        postAndExpect200(context)
    }

    @Test
    void WaitFirst_1Downstream_1Response_Expect200(TestContext context) {
        broadcast = startNewBroadcast("conf-1downstream-1response-waitfirst.json")
        postAndExpect200(context)
    }

    @Test
    void WaitFirst_3Downstreams_1Response_Expect200(TestContext context) {
        broadcast = startNewBroadcast("conf-3downstreams-1response-waitfirst.json")
        postAndExpect200(context)
    }

    @Test
    void WaitFirst_1Downstream_FailResponse_Expect500(TestContext context) {
        broadcast = startNewBroadcast("conf-1downstream-failresponse-waitfirst.json")
        postAndExpect500(context)
    }

    @Test
    void Check_UriMappings(TestContext context) {
        broadcast = startNewBroadcast("conf-uri-mappings.json")
        postAndExpectStatus(200, "/ok", context)
        //now everything is 500 if not ok
        postAndExpectStatus(500, "/redirect", context)
        postAndExpectStatus(500, "/error", context)
    }

    private def HttpBroadcast startNewBroadcast(String json) {
        new HttpBroadcast(vertx, new ResourceConfReader(json)).startSync()
    }

    private static def assertSuccess0 = { context, ar -> context.assertTrue(ar.succeeded()) }
    private static def assertSuccess = { context -> assertSuccess0.curry(context) }

    private static def createServer = { vertx, context, port, handler ->
        vertx.createHttpServer()
                .requestHandler(handler)
                .listen(port, 'localhost', assertSuccess)
    }

    private def postAndExpectStatus = { int status, String uri, TestContext context  ->
        Async async = context.async()
        client.post(8888, 'localhost', uri)
                .handler({ resp ->
            context.assertEquals(status, resp.statusCode())
            async.complete()
        })
        .setChunked(true)
        .end('Hi, all')
    }

    private def postAndExpect200 = postAndExpectStatus.curry(200).curry("/")
    private def postAndExpect500 = postAndExpectStatus.curry(500).curry("/")
}
