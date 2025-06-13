package telran.java57.farmmarket.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import telran.java57.farmmarket.model.Product;

import java.util.List;

public interface ProductRepository extends MongoRepository<Product,String> {
    List<Product> findByCategory(String category);
}
