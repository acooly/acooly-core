package com.acooly.module.springcloud;

import org.apache.http.client.HttpClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Zhouxi O_o
 *
 * @author xi
 * @description
 */
@Configuration
public class SpringcloudAutoConfiguration {

    @Bean
    @LoadBalanced
    public RestTemplate template(HttpClient httpClient) {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setHttpClient(httpClient);
        RestTemplate template = new RestTemplate(factory);
        return template;
    }

}
