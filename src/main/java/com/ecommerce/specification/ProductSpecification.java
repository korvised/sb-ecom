package com.ecommerce.specification;

import com.ecommerce.model.Category;
import com.ecommerce.model.Product;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class ProductSpecification implements Specification<Product> {

    private final String search;
    private final Category category;

    @Override
    public Predicate toPredicate(@NonNull Root<Product> root, CriteriaQuery<?> query, @NonNull CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        // Search condition for productName and description
        if (search != null && !search.isEmpty()) {
            // Create a condition to search for the productName
            Predicate namePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("productName")), "%" + search.toLowerCase() + "%");

            // Create a condition to search for the description
            Predicate descriptionPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%" + search.toLowerCase() + "%");

            // Add the OR condition for both productName and description
            predicates.add(criteriaBuilder.or(namePredicate, descriptionPredicate));
        }

        // Filter by category if it is provided
        if (category != null) {
            // Add the condition to filter by category
            predicates.add(criteriaBuilder.equal(root.get("category"), category));
        }

        // Combine all predicates with an AND condition and return
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
