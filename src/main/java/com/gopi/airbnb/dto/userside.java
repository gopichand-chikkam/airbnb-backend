spring.kafka.bootstrap-servers=localhost:9092
app.kafka.topic.user-registered=user-registered

spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer
spring.kafka.producer.value-serializer=org.springframework.kafka.support.serializer.JsonSerializer

  //config file
  package com.UserService.UserMicroservice.Configuration;


import com.UserService.UserMicroservice.UserServiceEvent.UserRegisteredEvent;


import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;

import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;



import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {
    @Value("${app.kafka.topic.user-registered}")
    private String userRegisteredTopic;
    @Bean
    public ProducerFactory<String, UserRegisteredEvent> producerFactory(){
        Map<String,Object> config= new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        return new DefaultKafkaProducerFactory<>(config, new StringSerializer(),new JsonSerializer<>());
    }

    @Bean
    public KafkaTemplate<String,UserRegisteredEvent> kafkaTemplate(ProducerFactory<String,UserRegisteredEvent>producerFactory){
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    public NewTopic userRegisteredTopic(){
      return  TopicBuilder.name("user-registered").partitions(1).replicas(1).build();

    }
}
/////////////////////////////
package com.UserService.UserMicroservice.Service;

import com.UserService.UserMicroservice.UserServiceEvent.UserRegisteredEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service

public class KafkaProducerService {
    @Value("${app.kafka.topic.user-registered}")
    private String userRegisteredTopic;

    @Autowired
    private final KafkaTemplate<String, UserRegisteredEvent> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String,UserRegisteredEvent>kafkaTemplate){
          this.kafkaTemplate=kafkaTemplate;
    }

    public void sendUserRegisteredEvent(UserRegisteredEvent event) {

        kafkaTemplate.send(userRegisteredTopic, event);
    }
}
