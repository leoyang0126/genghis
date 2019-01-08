package com.leoyang.genghis.kafka.spark.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.stream.IntStream;

/**
 * Created by yang.liu on 2018/12/17
 */
@Component
public class StartUtil implements ApplicationRunner {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    private static Logger logger = LoggerFactory.getLogger(StartUtil.class);

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        IntStream.rangeClosed(0,1000).forEach(index -> {
            try {
                logger.info("kafka的消息={}","message"+index);
                kafkaTemplate.send("test", "key", "message" + index);
                logger.info("发送kafka成功.");
            } catch (Exception e) {
                logger.error("发送kafka失败", e);
            }
        });
    }
}
