package com.cat;

import com.cat.config.ConfigProps;
import com.cat.dao.McDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.*;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class McApp {

    protected final Log logger = LogFactory.getLog(getClass());


    @Autowired
    ConfigProps configProps;

    public static void main(String[] args) {
        SpringApplication.run(McApp.class,args);
    }

    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageProducer inbound() {
        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter(configProps.getMqqtHost(), configProps.getClientId(),
                        configProps.getTopic());
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        adapter.setOutputChannel(mqttInputChannel());
        return adapter;
    }


    @Autowired
    McDao mcDao;

    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler handler(){
        return  new MessageHandler() {
            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
                MessageHeaders headers = message.getHeaders();
                //String mqtt_topic = configProps.getTopic();
                String mqtt_topic = (String) headers.get("mqtt_receivedTopic");
                String payload = (String) message.getPayload();
                logger.info(mqtt_topic);
                logger.info(payload);


                String[] split = payload.split("@");

                Map  map =new HashMap();
                map.put("dataid",split[0]);
                map.put("data",split[1]);


                mcDao.mergeInto(map);

            }
        };
    }

}
