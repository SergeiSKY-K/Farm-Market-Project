package telran.java57.farmmarket.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import telran.java57.farmmarket.dto.OrderDto;
import telran.java57.farmmarket.model.Order;

public interface OrderRepository extends MongoRepository<Order,String> {
}
