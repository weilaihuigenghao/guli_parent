package com.bdqn.staservice.schedule;

import com.bdqn.staservice.service.StatisticsDailyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTask {
    @Autowired
    private StatisticsDailyService staservice;

    //每隔5秒输出System.out.println("******task1执行了");这段话一次
   /* @Scheduled(cron = "0/5 * * * * ?")
    public void task1(){
        System.out.println("******task1执行了");

    }*/
    //在每天凌晨一点，执行方法，把数据查询进行添加
    @Scheduled(cron = "0 0 0 * * ?")//springboot,里面表达式只能是6位，7位会报错
    public void task2(){
        System.out.println("******task1执行了");

    }

}
