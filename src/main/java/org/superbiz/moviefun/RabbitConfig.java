package org.superbiz.moviefun;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.amqp.Amqp;
import org.superbiz.moviefun.albums.AlbumsUpdateMessageConsumer;

@Configuration
public class RabbitConfig {

    @Value("${rabbitmq.uri}")
    private String rabbitMqUri;

    @Value("${rabbitmq.queue}")
    private String rabbitMqQueue;

    @Bean
    public ConnectionFactory connectFactory(){
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setUri(rabbitMqUri);
        return connectionFactory;
    }

    @Bean
    public IntegrationFlow amqpInbound(ConnectionFactory connectionFactory, AlbumsUpdateMessageConsumer consumer){
        return IntegrationFlows
                .from(Amqp.inboundAdapter(connectionFactory, rabbitMqQueue))
                .handle(consumer :: consume)
                .get();
    }
}
