package com.gl.springsandbox.sse.service;

public interface EventStream<T> {
    <T> T getEventStream();

}
