package telran.java57.farmmarket.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import telran.java57.farmmarket.dto.CreateProductDto;
import telran.java57.farmmarket.dto.ResponseProductDto;
import telran.java57.farmmarket.dto.UpdateProductDto;
import telran.java57.farmmarket.service.ProductService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

    private final ProductService marketService;

    @PostMapping
    public ResponseProductDto createProduct(@RequestBody CreateProductDto dto){
        return marketService.addNewProduct(dto);
    }

    @PutMapping("/{id}")
    public ResponseProductDto updateProduct(@PathVariable String id, @RequestBody UpdateProductDto dto){
        return marketService.updateProduct(id,dto);
    }
    @GetMapping("/{id}")
    public ResponseProductDto getProduct(@PathVariable String id){
        return marketService.getProduct(id);
    }
    @GetMapping
    public List<ResponseProductDto>getAllProducts(){
        return marketService.getAllProducts();
    }
    @DeleteMapping("/{id}")
    public ResponseProductDto deleteProduct(@PathVariable String id){
        return marketService.deleteProduct(id);
    }
    @GetMapping("/category/{category}")
    public List<ResponseProductDto> getByCategory(@PathVariable String category){
        return marketService.getByCategory(category);
    }
}
