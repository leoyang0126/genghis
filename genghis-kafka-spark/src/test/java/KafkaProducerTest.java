import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.stream.IntStream;

/**
 * Created by yang.liu on 2018/12/17
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class KafkaProducerTest {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    private static Logger logger = LoggerFactory.getLogger(KafkaProducerTest.class);

    @Test
    public void test01() {

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
