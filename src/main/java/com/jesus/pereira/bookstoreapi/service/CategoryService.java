package com.jesus.pereira.bookstoreapi.service;

import com.jesus.pereira.bookstoreapi.domain.Category;

import java.util.List;


public interface CategoryService {

    Category findCategoryById(Long id);

    List<Category> findAllCategories();

    Category findCategoryByName(Long name);

    List<Category> findCategoriesByNameLike(Long name);

    Category createCategory(Category category);

    Category updateCategory(Category category, Long categoryId);

    void deleteCategory();


}
