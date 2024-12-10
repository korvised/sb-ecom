package com.ecommerce.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long productId;

    @NotBlank
    @Size(min = 3, message = "Product name must be at least 3 characters long")
    private String productName;
    private String image;

    @NotBlank
    @Size(min = 5, message = "Description must be at least 5 characters long")
    private String description;
    private Integer quantity;
    private Double price;
    private double discount;
    private Double specialPrice;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id")
    private Category category;
}
