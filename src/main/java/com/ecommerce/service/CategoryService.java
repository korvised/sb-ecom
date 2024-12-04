package com.ecommerce.service;

import com.ecommerce.model.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories();

    Category createCategory(Category category);

    Category updateCategory(long categoryId, Category category);

    void deleteCategory(long categoryId);
}
