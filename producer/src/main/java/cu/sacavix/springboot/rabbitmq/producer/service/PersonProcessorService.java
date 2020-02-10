package cu.sacavix.springboot.rabbitmq.producer.service;

import cu.sacavix.springboot.rabbitmq.producer.entity.Person;
import cu.sacavix.springboot.rabbitmq.producer.configuration.Constants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
@EnableScheduling
public class PersonProcessorService {

    private final ObjectMapper mapper = new ObjectMapper() ;
    
    public static final Logger logger = LoggerFactory.getLogger(PersonProcessorService.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * Receiving notifications to update the person
     */
    @RabbitListener(queues = Constants.PERSON_UPDATED_QUEUE)
    public void listener(String in) {
        try {
            Person person = mapper.readValue(in, Person.class);
            logger.info("Receiving notifications to update the person [{}] !", person.getId());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        System.out.println();


    }

    /**
     * Sending notifications of created person
     */
    @Scheduled(fixedDelay = 10000)
    public void updateOrder() {
        try {

            Person person = new Person() ;
            person.setId(UUID.randomUUID().toString());
            person.setName("Ypvillazon");
            person.setAge(30);

            logger.info("Sending notifications of created person: [{}] ", person.getId());
            rabbitTemplate.convertAndSend(Constants.PERSON_CREATED_QUEUE, mapper.writeValueAsString(person));
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
        }
    }

}
