package com.ecommerce.service;

import com.ecommerce.payload.CategoryDTO;
import com.ecommerce.payload.CategoryResponse;

public interface CategoryService {
    CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    CategoryDTO createCategory(CategoryDTO category);

    CategoryDTO updateCategory(long categoryId, CategoryDTO category);

    void deleteCategory(long categoryId);
}
