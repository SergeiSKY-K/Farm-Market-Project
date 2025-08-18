package telran.java57.farmmarket.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import telran.java57.farmmarket.model.Product;
import telran.java57.farmmarket.model.ProductStatus;

import java.util.List;

public interface ProductRepository extends MongoRepository<Product,String> {
List<Product> findByCategoryAndStatusOrderByCreatedAtDesc(String category, ProductStatus status);
    List<Product> findBySupplierLoginOrderByCreatedAtDesc(String supplierLogin);
    List<Product> findByStatusOrderByCreatedAtDesc(ProductStatus status);
    boolean existsByIdAndSupplierLogin(String id, String supplierLogin);
}
