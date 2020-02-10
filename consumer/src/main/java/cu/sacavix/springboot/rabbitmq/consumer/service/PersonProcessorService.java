package cu.sacavix.springboot.rabbitmq.consumer.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cu.sacavix.springboot.rabbitmq.consumer.configuration.Constants;
import cu.sacavix.springboot.rabbitmq.consumer.entity.Person;
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
     * Receiving notifications to create the person
     */
    @RabbitListener(queues = Constants.PERSON_CREATED_QUEUE)
    public void rabbitListener(String in) {
        try {
            Person person= mapper.readValue(in, Person.class);
            logger.debug("Receiving notifications to create the person [{}] !", person.getId());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println();
    }



    /**
     * Enviando un json en formato String UPDATE
     */
    @Scheduled(fixedDelay = 7000)
    public void updateOrder() {
        try {
            Person person = new Person() ;
            person.setId(UUID.randomUUID().toString());
            person.setName("Ypvillazon");
            person.setAge(30);

            logger.debug("Sending notifications of updated person: [{}] ", person.getId());
            rabbitTemplate.convertAndSend(Constants.PERSON_UPDATED_QUEUE, mapper.writeValueAsString(person));
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
        }
    }



}
