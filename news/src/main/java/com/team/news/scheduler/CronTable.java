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

    //서버 시작하고 6초후에 실행 후 30분마다 실행
    @Scheduled(initialDelay = 10000, fixedDelay = 1800000)
    public void Job() {
//        int total_cnt = 0;
//
//        Crawling_naver c;
//        try {
//            c = new Crawling_naver();
//            total_cnt += c.run(100, "정치");
//            total_cnt += c.run(101, "경제");
//            total_cnt += c.run(102, "사회");
//            total_cnt += c.run(103, "생활/문화");
//            total_cnt += c.run(104, "세계");
//            total_cnt += c.run(105, "IT/과학");
//            total_cnt += c.run_entertainment();
//            total_cnt += c.run_sports("kbaseball", "스포츠-야구");
//            total_cnt += c.run_sports("wbaseball", "스포츠-해외야구");
//            total_cnt += c.run_sports("kfootball", "스포츠-축구");
//            total_cnt += c.run_sports("wfootball", "스포츠-해외축구");
//            total_cnt += c.run_sports("basketball", "스포츠-농구");
//            total_cnt += c.run_sports("esports", "스포츠-e스포츠");
//        } catch (java.text.ParseException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        System.out.println("\r\n총 기사 개수 : "+total_cnt);
//
//        SimpleDateFormat date = new SimpleDateFormat("yyyy/MM/dd HH:mm ");
//        Calendar cal = Calendar.getInstance();    // 7시간 이내
//        System.out.println(date.format(cal.getTime()));
    }
}
