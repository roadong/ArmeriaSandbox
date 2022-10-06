package com.gl.springsandbox.sse.configuration;

import com.gl.springsandbox.sse.controller.SSEController;
import com.linecorp.armeria.client.ClientFactory;
import com.linecorp.armeria.client.circuitbreaker.CircuitBreakerClient;
import com.linecorp.armeria.client.circuitbreaker.CircuitBreakerRule;
import com.linecorp.armeria.common.CommonPools;
import com.linecorp.armeria.common.Flags;
import com.linecorp.armeria.common.RequestId;
import com.linecorp.armeria.common.reactor3.RequestContextHooks;
import com.linecorp.armeria.common.util.BlockingTaskExecutor;
import com.linecorp.armeria.common.util.EventLoopGroups;
import com.linecorp.armeria.server.cors.CorsService;
import com.linecorp.armeria.server.encoding.DecodingService;
import com.linecorp.armeria.server.encoding.EncodingService;
import com.linecorp.armeria.server.logging.ContentPreviewingService;
import com.linecorp.armeria.spring.ArmeriaServerConfigurator;
import com.linecorp.armeria.spring.web.reactive.ArmeriaClientConfigurator;
import io.netty.channel.EventLoopGroup;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;
import java.time.Duration;

@Configuration
public class ArmeriaServerConfigure {

    private final SSEController sseController;

    public ArmeriaServerConfigure(SSEController sseController) {
        this.sseController = sseController;
    }

    @Bean
    public static EventLoopGroup eventLoopGroup() {
        return EventLoopGroups.newEventLoopGroup(Flags.numCommonWorkers(), "armeria-eventloop-thread");
    }

    @Bean
    public static BlockingTaskExecutor blockingTaskExecutor() {
        return BlockingTaskExecutor.builder()
                // 나중에 Blocking Task 전후처리에 사용
                //.taskFunction()
                // 기본 블로킹 스레드는 60초
                //.keepAliveTime()
                .threadNamePrefix("long-task-blocking-thread")
                .numThreads(Flags.numCommonBlockingTaskThreads()).build();
    }

    @Bean
    public ArmeriaServerConfigurator armeriaServerConfigurator() {
        RequestContextHooks.enable();
        return serverBuilder -> {
            // 이벤트 루프 스레드 지정
            serverBuilder.workerGroup(eventLoopGroup(), true)
                    .startStopExecutor(CommonPools.workerGroup());
            serverBuilder.blockingTaskExecutor(blockingTaskExecutor().unwrap(), true);
            serverBuilder.requestIdGenerator(RequestId::random);
//            serverBuilder.decorator(LoggingService.newDecorator());

            // 60초 타임아웃
            serverBuilder.idleTimeout(Duration.ofSeconds(60));
            serverBuilder.requestTimeout(Duration.ofSeconds(60));
            //serverBuilder.verboseResponses(true);

            serverBuilder.decorator(EncodingService.newDecorator());
            serverBuilder.decorator(ContentPreviewingService.newDecorator(Integer.MAX_VALUE, StandardCharsets.UTF_8));
            serverBuilder.decorator(DecodingService.newDecorator());

            serverBuilder.decorator(CorsService.builderForAnyOrigin().newDecorator());

            serverBuilder.annotatedService(this.sseController);

            //노출 제한 헤더 off
            serverBuilder.disableServerHeader();
            serverBuilder.disableDateHeader();
        };
    }

    @Bean
    public ClientFactory clientFactory() {
        return ClientFactory.insecure();
    }

    // A user can configure a Client by providing an ArmeriaClientConfigurator bean.
    @Bean
    public ArmeriaClientConfigurator armeriaClientConfigurator(ClientFactory clientFactory) {

        // Customize the client using the given WebClientBuilder. For example:
        return builder -> {
            // Use a circuit breaker for each remote host.
            final CircuitBreakerRule rule =
                    CircuitBreakerRule.onServerErrorStatus();
            builder.decorator(CircuitBreakerClient.builder(rule)
                    .newDecorator());

            // Set a custom client factory.
            builder.factory(clientFactory);
        };
    }
}
