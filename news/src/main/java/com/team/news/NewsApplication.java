package com.team.news;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@SpringBootApplication
@EnableScheduling   // 작업 스케쥴러 활성화
public class NewsApplication {

    public static void main(String[] args) {
        SpringApplication.run(NewsApplication.class, args);
    }

    // 단일 쓰레드를 사용할 경우
    @Bean
    public TaskScheduler taskScheduler() {
        return new ConcurrentTaskScheduler();
    }


//    // 2개 이상의 작업을 동시에 할 경우
//    @Bean
//    public TaskScheduler taskScheduler() {
//        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
//        taskScheduler.setPoolSize(5);     // 최대 5개
//
//        return taskScheduler;
//    }
}
