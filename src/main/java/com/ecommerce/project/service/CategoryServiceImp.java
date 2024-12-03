package com.ecommerce.project.service;

import com.ecommerce.project.model.Category;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImp implements CategoryService {

    private final List<Category> categories = new ArrayList<>();
    private long nextId = 1;

    @Override
    public List<Category> getAllCategories() {
        return categories;
    }

    @Override
    public Category createCategory(Category category) {
        category.setCategoryId(nextId++);
        categories.add(category);
        return category;
    }

    @Override
    public Category updateCategory(long categoryId, Category category) {
        Category categoryToUpdate = categories.stream()
                .filter(c -> c.getCategoryId() == categoryId)
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        categoryToUpdate.setCategoryName(category.getCategoryName());
        return categoryToUpdate;
    }

    @Override
    public void deleteCategory(long categoryId) {
        Category category = categories.stream()
                .filter(c -> c.getCategoryId() == categoryId)
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        categories.remove(category);
    }
}
