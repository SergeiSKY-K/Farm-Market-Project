package telran.java57.farmmarket.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import telran.java57.farmmarket.dao.ProductRepository;
import telran.java57.farmmarket.dto.CreateProductDto;
import telran.java57.farmmarket.dto.ResponseProductDto;
import telran.java57.farmmarket.dto.UpdateProductDto;
import telran.java57.farmmarket.dto.exceptions.ProductNotFoundException;
import telran.java57.farmmarket.model.Product;
import telran.java57.farmmarket.model.ProductStatus;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    final ProductRepository productRepository;
    final ModelMapper modelMapper;

    @Override
    public ResponseProductDto addNewProduct(CreateProductDto dto) {
        Product product =modelMapper.map(dto,Product.class);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String supplierLogin = authentication.getName();
        product.setSupplierLogin(supplierLogin);
        product.setStatus(ProductStatus.ACTIVE);
        product = productRepository.save(product);
        return modelMapper.map(product,ResponseProductDto.class);
    }

    @Override
    public ResponseProductDto updateProduct(String id, UpdateProductDto dto) {
        Product product = productRepository.findById(id).orElseThrow(()-> new ProductNotFoundException(id));

        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!currentUser.equals(product.getSupplierLogin())) {
            throw new AccessDeniedException("You can update only your own products.");
        }
        modelMapper.map(dto,product);
        product = productRepository.save(product);
        return modelMapper.map(product,ResponseProductDto.class);
    }

    @Override
    public ResponseProductDto getProduct(String id) {
       Product product = productRepository.findById(id).orElseThrow(()->new ProductNotFoundException(id));
        return modelMapper.map(product,ResponseProductDto.class);
    }

    @Override
    public List<ResponseProductDto> getAllProducts() {
        List<Product> products = productRepository.findByStatus(ProductStatus.ACTIVE);
        return products.stream()
                .map(product -> modelMapper.map(product, ResponseProductDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public ResponseProductDto deleteProduct(String id) {
        Product product = productRepository.findById(id).orElseThrow(()-> new ProductNotFoundException(id));

        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!currentUser.equals(product.getSupplierLogin())) {
            throw new AccessDeniedException("You can delete only your own products.");
        }

        productRepository.deleteById(id);
        return modelMapper.map(product,ResponseProductDto.class);
    }

    @Override
    public List<ResponseProductDto> getByCategory(String category) {
        List <Product> products = productRepository.findByCategory(category);
        return products.stream()
                .map(product -> modelMapper.map(product,ResponseProductDto.class))
                .toList();
    }

    @Override
    public List<ResponseProductDto> getProductsBySupplier(String supplierLogin) {
        List<Product> products = productRepository.findBySupplierLogin(supplierLogin);
        return products.stream()
                .map(product -> modelMapper.map(product, ResponseProductDto.class))
                .toList();
    }

    @Override
    public ResponseProductDto toggleProductStatus(String id, boolean block) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        product.setStatus(block ? ProductStatus.BLOCKED : ProductStatus.ACTIVE);
        productRepository.save(product);

        return modelMapper.map(product, ResponseProductDto.class);
    }
}
