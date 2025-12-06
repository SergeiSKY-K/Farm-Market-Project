package telran.java57.farmmarket.service.kafka.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import telran.java57.farmmarket.events.ProductCreatedEvent;
import telran.java57.farmmarket.events.ProductDeletedEvent;
import telran.java57.farmmarket.events.ProductUpdatedEvent;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {
    private final KafkaTemplate<String,Object> kafkaTemplate;

    private static final String PRODUCT_CREATED_TOPIC = "product-created";
    private static final String PRODUCT_UPDATED_TOPIC = "product-updated";
    private static final String PRODUCT_DELETED_TOPIC = "product-deleted";

    public void sendCreated(ProductCreatedEvent event) {
        kafkaTemplate.send(PRODUCT_CREATED_TOPIC, event);
        System.out.println("📤 Created event to Kafka: " + event);
    }

    public void sendUpdated(ProductUpdatedEvent event) {
        kafkaTemplate.send(PRODUCT_UPDATED_TOPIC, event);
        System.out.println("📤 Updated event to Kafka: " + event);
    }

    public void sendDeleted(ProductDeletedEvent event) {
        kafkaTemplate.send(PRODUCT_DELETED_TOPIC, event);
        System.out.println("📤 Deleted event to Kafka: " + event);
    }
}
