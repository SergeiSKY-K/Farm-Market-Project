package telran.java57.farmmarket.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import telran.java57.farmmarket.dto.OrderDto;
import telran.java57.farmmarket.model.Order;

import java.util.Collection;
import java.util.List;

public interface OrderRepository extends MongoRepository<Order,String> {
    @Query("{ 'productsId': { $in: ?0 } }")
    List<Order> findByProductIdsContainingAny(Collection<String> productIds);
}
