package com.indocyber.springboot;

import com.launchdarkly.eventsource.EventHandler;
import com.launchdarkly.eventsource.EventSource;
import lombok.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.concurrent.TimeUnit;

@Service
public class WikimediaChangeProducer {

//    private String topic;
    public static final Logger LOGGER = LoggerFactory.getLogger(WikimediaChangeProducer.class);

    private KafkaTemplate<String,String> kafkaTemplate;

    public WikimediaChangeProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage() throws InterruptedException {

        //String topic ="wikimedia_recentchange";

        //to read stream real time data for wikimedia, we use event source
        EventHandler eventHandler = new WikimediaChangesHandler(kafkaTemplate, "wikimedia_recentchange");
        String url = "https://stream.wikimedia.org/v2/stream/recentchange";
        EventSource.Builder builder = new EventSource.Builder(eventHandler, URI.create(url));
        EventSource eventSource = builder.build();
        eventSource.start();

        TimeUnit.MINUTES.sleep(10);

    }
}
