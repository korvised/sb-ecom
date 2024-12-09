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

@AllArgsConstructor
public class ProductSpecification implements Specification<Product> {

    private final String search;
    private final Category category;

    @Override
    public Predicate toPredicate(@NonNull Root<Product> root, CriteriaQuery<?> query, @NonNull CriteriaBuilder criteriaBuilder) {
        Predicate finalPredicate = criteriaBuilder.conjunction();

        // Search condition for productName and description
        if (search != null && !search.isEmpty()) {
            Predicate namePredicate = criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("productName")),
                    "%" + search.toLowerCase() + "%"
            );
            Predicate descriptionPredicate = criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("description")),
                    "%" + search.toLowerCase() + "%"
            );

            // Combine the two conditions with OR
            finalPredicate = criteriaBuilder.and(
                    finalPredicate, // "existing" predicate (starts as true)
                    criteriaBuilder.or(namePredicate, descriptionPredicate) // combine with new condition
            );
        }

        // Filter by category if it is provided
        if (category != null) {
            Predicate categoryPredicate = criteriaBuilder.equal(root.get("category"), category);

            // Combine the category filter with the existing predicates using AND
            finalPredicate = criteriaBuilder.and(finalPredicate, categoryPredicate);
        }

        return finalPredicate;
    }
}
