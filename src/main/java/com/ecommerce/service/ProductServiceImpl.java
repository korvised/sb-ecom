package com.ecommerce.service;

import com.ecommerce.config.AppConstants;
import com.ecommerce.exceptions.ApiException;
import com.ecommerce.exceptions.ResourceNotFoundException;
import com.ecommerce.model.Category;
import com.ecommerce.model.Product;
import com.ecommerce.payload.ProductDTO;
import com.ecommerce.payload.ProductResponse;
import com.ecommerce.repository.CategoryRepository;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.specification.ProductSpecification;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@AllArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    @Override
    public ProductResponse getAllProducts(String search, Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        // Fetch the category if categoryId is provided
        Category category = (categoryId != null)
                ? categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "CategoryId", categoryId))
                : null;

        // Determine the sort order
        Sort sort = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // Define pagination details
        Pageable pageDetails = PageRequest.of(pageNumber - 1, pageSize, sort);

        // Create the ProductSpecification with search and category
        ProductSpecification specification = new ProductSpecification(search, category);

        // Retrieve products with filtering (search and category)
        Page<Product> productPage = productRepository.findAll(specification, pageDetails);

        // Map entities to DTOs
        List<ProductDTO> productDTOs = productPage.getContent()
                .stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOs);
        productResponse.setPageNumber(productPage.getNumber() + 1);
        productResponse.setPageSize(productPage.getSize());
        productResponse.setTotalElements(productPage.getTotalElements());
        productResponse.setTotalPages(productPage.getTotalPages());
        productResponse.setLastPage(productPage.isLast());

        return productResponse;
    }

    @Override
    public ProductDTO getProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "ProductId", productId));

        return modelMapper.map(product, ProductDTO.class);
    }

    @Override
    public ProductDTO addProduct(ProductDTO productDTO, Long categoryId) {
        Category category = categoryRepository.findById(categoryId).
                orElseThrow(() -> new ResourceNotFoundException("Category", "CategoryId", categoryId));

        // Calculate special price
        double specialPrice = productDTO.getPrice() - ((productDTO.getDiscount() * 0.01) * productDTO.getPrice());

        Product product = modelMapper.map(productDTO, Product.class);
        product.setImage("default.png");
        product.setSpecialPrice(specialPrice);
        product.setCategory(category);

        Product savedProduct = productRepository.save(product);

        return modelMapper.map(savedProduct, ProductDTO.class);
    }

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile image) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "ProductId", productId));

        // Save the image to the file system
        String fileName = uploadImage(image);

        // Update the product image
        existingProduct.setImage(fileName);

        Product updatedProduct = productRepository.save(existingProduct);

        return modelMapper.map(updatedProduct, ProductDTO.class);
    }

    @Override
    public ProductDTO updateProduct(Long productId, ProductDTO productDTO) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "ProductId", productId));

        // Calculate special price
        double specialPrice = productDTO.getPrice() - ((productDTO.getDiscount() * 0.01) * productDTO.getPrice());

        existingProduct.setProductName(productDTO.getProductName());
        existingProduct.setDescription(productDTO.getDescription());
        existingProduct.setPrice(productDTO.getPrice());
        existingProduct.setDiscount(productDTO.getDiscount());
        existingProduct.setSpecialPrice(specialPrice);

        Product updatedProduct = productRepository.save(existingProduct);

        return modelMapper.map(updatedProduct, ProductDTO.class);
    }

    @Override
    public void deleteProduct(Long productId) {
        productRepository.findById(productId)
                .ifPresentOrElse(
                        productRepository::delete,
                        () -> {
                            throw new ResourceNotFoundException("Product", "ProductId", productId);
                        }
                );
    }


    private String uploadImage(MultipartFile image) {
        // File name of current / original image
        String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(image.getOriginalFilename()));

        // Generate a unique file name
        String randomId = UUID.randomUUID().toString();
        String fileName = randomId.concat(originalFileName.substring(originalFileName.lastIndexOf(".")));
        String path = AppConstants.IMAGE_UPLOAD_PATH + File.separator + fileName;

        System.out.println("Path: " + path);

        // Check if path exists, if not create the directory
        File folder = new File(AppConstants.IMAGE_UPLOAD_PATH);
        if (!folder.exists()) {
            System.out.println("Creating directory to upload image");
            boolean created = folder.mkdirs();
            if (!created) {
                throw new ApiException("Failed to create directory to upload image");
            }
        }

        // Copy the image to the directory
        try {
            Files.copy(image.getInputStream(), Paths.get(path));
        } catch (IOException e) {
            System.out.println("Failed to upload image " + e);
            throw new ApiException("Failed to upload image " + e.getMessage());
        }

        return fileName;
    }
}