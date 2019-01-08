package com.leoyang.genghis.kafka.spark.util;

import com.leoyang.genghis.kafka.spark.util.sparkstream.SparkKafkaStreamExecutor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.stereotype.Component;

/**
 * spring boot 容器加载完成后执行
 * 启动kafka数据接收和处理
 * Created by yang.liu on 2018/12/17
 *
 */
@Component
public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        System.out.println("启动ApplicationStartup");
        ApplicationContext ac = event.getApplicationContext();
        SparkKafkaStreamExecutor sparkKafkaStreamExecutor= ac.getBean(SparkKafkaStreamExecutor.class);
        Thread thread = new Thread(sparkKafkaStreamExecutor);
        thread.start();
    }

}