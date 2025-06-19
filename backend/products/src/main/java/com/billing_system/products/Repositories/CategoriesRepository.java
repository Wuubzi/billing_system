package com.billing_system.products.Repositories;

import com.billing_system.products.Entities.Categories;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriesRepository extends JpaRepository<Categories, Long> {
}
