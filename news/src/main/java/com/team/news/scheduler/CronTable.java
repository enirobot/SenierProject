package com.team.news.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


/**
 * 스케줄러가 할 일 정의
 */
@Component
public class CronTable {

//    // 매일 21시 30분 0초에 실행
//    @Scheduled(cron = "0 30 21 * * *")
//    public void dayJob() {
//
//    }

//    // 매월 1일 0시 0분 0초에 실행
//    @Scheduled(cron = "0 0 0 1 * *")
//    public void monthJob() {
//
//    }

    @Scheduled(initialDelay = 6000, fixedDelay = 1800000)
    public void Job() {

    }
}
