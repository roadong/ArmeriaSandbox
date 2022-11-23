package com.gl.springsandbox.api.security.token;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthenticationTokenHelper {

    private static final String TOKEN_KEY = "_atc";
    //private final Long cookieAge = 60 * 60 * 24L;

    public static void setAuthTokeCookie(HttpServletResponse response, Cookie cookie) {
        response.addCookie(cookie);
    }

    public static void setAuthTokeCookie(HttpServletResponse response, String cookieValue) {
        setAuthTokeCookie(response, generateAuthCookie(cookieValue));
    }

    public static Cookie generateAuthCookie(String tokenValue) {
        Cookie cookie = new Cookie(TOKEN_KEY, tokenValue);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");

        return cookie;
    }
}
