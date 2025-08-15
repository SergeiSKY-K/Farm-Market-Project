package telran.java57.farmmarket.security.checks;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import telran.java57.farmmarket.dao.ProductRepository;

@Component("productSecurity")
@RequiredArgsConstructor
public class ProductSecurity {
    private final ProductRepository productRepository;

    public boolean isOwner(String productId, String username) {
        return productRepository.existsByIdAndSupplierLogin(productId, username);
    }
}
