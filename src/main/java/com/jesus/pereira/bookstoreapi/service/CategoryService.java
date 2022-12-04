package com.jesus.pereira.bookstoreapi.service;

import com.jesus.pereira.bookstoreapi.domain.Category;
import com.jesus.pereira.bookstoreapi.resource.dto.CategoryDTO;

import java.util.List;
import java.util.Optional;


public interface CategoryService {

    Category findCategoryById(Long id);

    List<Category> findAllCategories();

    List<Category> findCategoriesByNameLike(String name);

    Category createCategory(CategoryDTO categoryDTO);

    Category updateCategory(CategoryDTO categoryDTO, Long categoryId);

    void deleteCategory(Long categoryId);


}
