package com.aker.TechNews.Config;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Configuration
public class RateLimiterCustomConfig {

    @Autowired
    private RateLimiterRegistry rateLimiterregistry;

    @Bean("rateLimiterConfiguration_TechNews")
    public RateLimiter rateLimiterCustomConfig(){
        RateLimiterConfig rateLimiterConfig = RateLimiterConfig.custom()
                .limitForPeriod(15)
                .limitRefreshPeriod(Duration.of(15, ChronoUnit.MINUTES))
                .timeoutDuration(Duration.of(15,ChronoUnit.SECONDS))
                .build();

        return rateLimiterregistry.rateLimiter("TechNewsLimiter", rateLimiterConfig);
    }

    public void updateRateLimiterConfiguration(String rateLimiterName, int limitForPeriod, Duration timeoutDuration){
        RateLimiter limiter = rateLimiterregistry.rateLimiter(rateLimiterName);
        limiter.changeLimitForPeriod(limitForPeriod);
        limiter.changeTimeoutDuration(timeoutDuration);
    }
}
