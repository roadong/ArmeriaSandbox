package com.gl.springsandbox.scg.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.sleuth.CurrentTraceContext;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.cloud.sleuth.http.HttpServerHandler;
import org.springframework.cloud.sleuth.instrument.web.TraceWebFilter;
import org.springframework.stereotype.Component;

//@Component
//@Slf4j
//public class GlobalFilter extends TraceWebFilter {
//    public GlobalFilter(Tracer tracer,
//                        HttpServerHandler handler,
//                        CurrentTraceContext currentTraceContext) {
//        super(tracer, handler, currentTraceContext);
//    }
//}
