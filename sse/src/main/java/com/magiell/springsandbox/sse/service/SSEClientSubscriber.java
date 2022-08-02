package com.magiell.springsandbox.sse.service;

import com.linecorp.armeria.common.sse.ServerSentEvent;
import com.linecorp.armeria.common.util.TimeoutMode;
import com.linecorp.armeria.server.ServiceRequestContext;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.SignalType;

import java.time.Duration;

@Slf4j
public class SSEClientSubscriber extends BaseSubscriber<ServerSentEvent> {

    @Override
    protected void hookOnSubscribe(Subscription subscription) {
        log.info("### hook subscribe event : {}", subscription.toString());
        super.hookOnSubscribe(subscription);
    }

    @Override
    public boolean isDisposed() {
        return super.isDisposed();
    }

    @Override
    public void dispose() {
        log.info("### stream cancel");
        super.dispose();
    }

    //    @Override
//    protected void hookOnNext(ServerSentEvent value) {
//        log.info("### hook next stream data : {}", value);
//        super.hookOnNext(value);
//    }

    @Override
    protected void hookOnComplete() {
        log.info("### hook stream complete");
        super.hookOnComplete();
    }

    @Override
    protected void hookOnError(Throwable throwable) {
        log.error("### hook stream error", throwable);
        super.hookOnError(throwable);
    }

    @Override
    protected void hookOnCancel() {
        log.info("### hook stream cancel");
        super.hookOnCancel();
    }

//    @Override
//    protected void hookFinally(SignalType type) {
//
//    }
}
