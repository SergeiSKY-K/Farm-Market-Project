package telran.java57.farmmarket.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import telran.java57.farmmarket.dao.ProductRepository;
import telran.java57.farmmarket.dto.CreateProductDto;
import telran.java57.farmmarket.dto.ResponseProductDto;
import telran.java57.farmmarket.dto.UpdateProductDto;
import telran.java57.farmmarket.dto.exceptions.ProductNotFoundException;
import telran.java57.farmmarket.model.Product;
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
        product = productRepository.save(product);
        return modelMapper.map(product,ResponseProductDto.class);
    }

    @Override
    public ResponseProductDto updateProduct(String id, UpdateProductDto dto) {
        Product product = productRepository.findById(id).orElseThrow(()-> new ProductNotFoundException(id));
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
        List <Product> products = productRepository.findAll();
        return products.stream()
                .map(product -> modelMapper.map(product,ResponseProductDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public ResponseProductDto deleteProduct(String id) {
        Product product = productRepository.findById(id).orElseThrow(()-> new ProductNotFoundException(id));
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
}
