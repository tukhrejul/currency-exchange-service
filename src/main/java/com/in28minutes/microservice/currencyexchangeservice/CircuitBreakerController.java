package com.in28minutes.microservice.currencyexchangeservice;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class CircuitBreakerController {

    private Logger logger = LoggerFactory.getLogger(CircuitBreakerController.class);

    @GetMapping("sample-api")
   // @Retry(name = "sample-api", fallbackMethod = "hardCodedMethod")
    @CircuitBreaker(name = "default", fallbackMethod = "hardCodedMethod")
    @RateLimiter(name = "default")
    @Bulkhead(name = "sample-api")
    public String getSampleApi()
    {
        logger.info("Sample Api call received");
        ResponseEntity<String> forEntity = new RestTemplate().getForEntity("http://localhost:8000/dummy-url", String.class);
        return forEntity.getBody();
    }

    public String hardCodedMethod(Exception ex)
    {
        logger.error("Unable to call the uri");
       return "Fallback API Called";
    }
}
