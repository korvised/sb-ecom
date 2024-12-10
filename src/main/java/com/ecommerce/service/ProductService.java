package com.ecommerce.service;

import com.ecommerce.payload.ProductDTO;
import com.ecommerce.payload.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {
    ProductResponse getAllProducts(String search, Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductDTO getProductById(Long productId);

    ProductDTO addProduct(ProductDTO productDTO, Long categoryId);

    ProductDTO updateProductImage(Long productId, MultipartFile image);

    ProductDTO updateProduct(Long productId, ProductDTO productDTO);

    void deleteProduct(Long productId);
}
