package com.avengers.gamera;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@EnableScheduling
@SpringBootApplication
@EnableAsync
public class GameraApplication {
    private static final String TIME_ZONE = "UTC";

    public static void main(String[] args) {
        SpringApplication.run(GameraApplication.class, args);
    }

    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone(TIME_ZONE));
    }
}
