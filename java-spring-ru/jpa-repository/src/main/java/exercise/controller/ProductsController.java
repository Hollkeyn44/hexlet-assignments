package exercise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;
import java.util.Comparator;

import exercise.model.Product;
import exercise.repository.ProductRepository;
import exercise.exception.ResourceNotFoundException;

@RestController
@RequestMapping("/products")
public class ProductsController {

    @Autowired
    private ProductRepository productRepository;

    // BEGIN
    @GetMapping(path = "")
    public List<Product> getProducts(@RequestParam("min") Optional<Integer> min, @RequestParam("max") Optional<Integer> max) {
        List<Product> products = productRepository.findAll();
        if (min.isPresent() && max.isPresent()) {
            products = productRepository.findByPriceBetween(min.get(), max.get());
        } else if (min.isPresent() ) {
            products = productRepository.findByPriceGreaterThan(min.get());
        } else if (max.isPresent() ) {
            products = productRepository.findByPriceLessThan(max.get());
        }
        products.sort(Comparator.comparingInt(Product::getPrice));
        return products;
    }
    // END

    @GetMapping(path = "/{id}")
    public Product show(@PathVariable long id) {

        var product =  productRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " not found"));

        return product;
    }
}
