package telran.java57.farmmarket.service;

import telran.java57.farmmarket.dto.CreateProductDto;
import telran.java57.farmmarket.dto.ResponseProductDto;
import telran.java57.farmmarket.dto.UpdateProductDto;

import java.util.List;

public interface ProductService {
    ResponseProductDto addNewProduct(CreateProductDto dto);

    ResponseProductDto updateProduct(String id, UpdateProductDto dto);

    ResponseProductDto getProduct(String id);

    List<ResponseProductDto> getAllProducts();

    ResponseProductDto deleteProduct(String id);

    List<ResponseProductDto> getByCategory(String category);
}
