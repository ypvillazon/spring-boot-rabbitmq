package cu.sacavix.springboot.rabbitmq.producer.configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


@Component
public class Configuration {

    @Bean
    public Queue queue1() {
        return new Queue(Constants.PERSON_CREATED_QUEUE);
    }

    @Bean
    public Queue queue2() {
        return new Queue(Constants.PERSON_UPDATED_QUEUE);
    }


}
