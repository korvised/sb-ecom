package com.ecommerce.service;

import com.ecommerce.model.Category;
import com.ecommerce.payload.CategoryResponse;

import java.util.List;

public interface CategoryService {
    CategoryResponse getAllCategories();

    Category createCategory(Category category);

    Category updateCategory(long categoryId, Category category);

    void deleteCategory(long categoryId);
}
