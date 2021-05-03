package com.example.shop.schedule;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;

@Component
public class LogClearScheduler {

    @Scheduled(cron = "0 0 */12 ? * *")
    public void clearRequestTrackerLogEveryOneMonth() throws IOException {
        new FileWriter("src/main/java/com/example/shop/log/RequestTracker.log", false).close();
    }
}