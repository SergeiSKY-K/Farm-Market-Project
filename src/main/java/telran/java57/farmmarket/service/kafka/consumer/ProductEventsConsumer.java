package telran.java57.farmmarket.service.kafka.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import telran.java57.farmmarket.events.ProductCreatedEvent;
import telran.java57.farmmarket.events.ProductDeletedEvent;
import telran.java57.farmmarket.events.ProductUpdatedEvent;

@Service
@Slf4j
public class ProductEventsConsumer {
    @KafkaListener(topics = "product-created",groupId = "product-service")
    public void onProductCreated(ProductCreatedEvent event) {
        log.info("[Kafka] Product CREATED: {}", event);
    }

    @KafkaListener(topics = "product-deleted",groupId = "product-service")
    public void onProductDeleted(ProductDeletedEvent event){
        log.info("[Kafka] Product DELETED: {}",event);
    }

    @KafkaListener(topics = "product-updated",groupId = "product-service")
    public void onProductUpdated(ProductUpdatedEvent event){
        log.info("[Kafka] Product UPDATED: {}",event);
    }
}
