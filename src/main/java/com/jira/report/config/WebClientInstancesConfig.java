package com.jira.report.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

import java.util.concurrent.TimeUnit;


@Configuration
public class WebClientInstancesConfig {
    private JiraReportConfigApi jiraReportConfigApi;

    public WebClientInstancesConfig(JiraReportConfigApi jiraReportConfigApi) {
        this.jiraReportConfigApi = jiraReportConfigApi;
    }

    @Bean
    public WebClient jiraWebClient(ReactiveServicesExchangesConfig reactiveServicesExchangesConfig) {
        TcpClient timeoutClient = TcpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, reactiveServicesExchangesConfig.getConnectionTimeout())
                .doOnConnected(
                        c -> c.addHandlerLast(new ReadTimeoutHandler(reactiveServicesExchangesConfig.getReadTimeout(), TimeUnit.MILLISECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(reactiveServicesExchangesConfig.getReadTimeout(), TimeUnit.MILLISECONDS))
                );

        WebClient.Builder webClientBuilder = WebClient
                .builder();
        return webClientBuilder
                .clientConnector(new ReactorClientHttpConnector(HttpClient.from(timeoutClient)))
                .exchangeStrategies(ExchangeStrategies.builder().codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024)).build())
                .filter(ExchangeFilterFunctions.basicAuthentication(jiraReportConfigApi.getUsername(), jiraReportConfigApi.getJiraToken()))
                .build();
    }

    @Bean
    public WebClient tempoWebClient(ReactiveServicesExchangesConfig reactiveServicesExchangesConfig) {
        TcpClient timeoutClient = TcpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, reactiveServicesExchangesConfig.getConnectionTimeout())
                .doOnConnected(
                        c -> c.addHandlerLast(new ReadTimeoutHandler(reactiveServicesExchangesConfig.getReadTimeout(), TimeUnit.MILLISECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(reactiveServicesExchangesConfig.getReadTimeout(), TimeUnit.MILLISECONDS))
                );

        WebClient.Builder webClientBuilder = WebClient
                .builder();
        return webClientBuilder
                .clientConnector(new ReactorClientHttpConnector(HttpClient.from(timeoutClient)))
                .exchangeStrategies(ExchangeStrategies.builder().codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024)).build())
                .defaultHeaders(h -> h.setBearerAuth(jiraReportConfigApi.getTempoToken()))
                .build();
    }

}
