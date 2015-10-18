package com.alexrnv.httpbroadcast.common

/**
 * Http related constants and utilities
 * @author: Alex
 */
class HttpCode {
    static final int HTTP_CODE_OK = 200
    static final int HTTP_CODE_REDIRECT = 300
    static final int HTTP_CODE_ERR = 500

    static boolean isCodeOk(int code) {
        code >= HTTP_CODE_OK && code < HTTP_CODE_REDIRECT
    }
}
