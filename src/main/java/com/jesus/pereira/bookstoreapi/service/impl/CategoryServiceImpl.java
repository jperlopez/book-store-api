package com.jesus.pereira.bookstoreapi.service.impl;

import com.jesus.pereira.bookstoreapi.domain.Category;
import com.jesus.pereira.bookstoreapi.exception.CategoryAlreadyExistsException;
import com.jesus.pereira.bookstoreapi.exception.NoSuchElementExistsException;
import com.jesus.pereira.bookstoreapi.mapper.AuthorMapper;
import com.jesus.pereira.bookstoreapi.mapper.CategoryMapper;
import com.jesus.pereira.bookstoreapi.repository.CategoryRepository;
import com.jesus.pereira.bookstoreapi.resource.dto.CategoryDTO;
import com.jesus.pereira.bookstoreapi.service.CategoryService;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.CannotCreateTransactionException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private static final String CATEGORY_ALREADY_EXISTS_EXCEPTION = "Category with name %1$s ";
    private static final String NO_SUCH_ELEMENT_EXISTS_EXCEPTION = "No category exists with id %s";

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public Category findCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementExistsException(String.format(NO_SUCH_ELEMENT_EXISTS_EXCEPTION, id)));
    }

    @Override
    public List<Category> findAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public List<Category> findCategoriesByNameLike(String name) {
        return categoryRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public Category createCategory(CategoryDTO categoryDTO) {
        categoryRepository.findByNameIgnoreCase(categoryDTO.getName())
                .ifPresent(err -> {
                    throw new CategoryAlreadyExistsException(String.format(CATEGORY_ALREADY_EXISTS_EXCEPTION, categoryDTO.getName()));
                });

        Category categoryToPersist = categoryMapper.toCategory(categoryDTO);
        return categoryRepository.save(categoryToPersist);
    }

    @Override
    public Category updateCategory(CategoryDTO categoryDTO, Long categoryId) {
        return categoryRepository.findById(categoryId)
                .map(category -> categoryRepository.save(categoryMapper.toCategoryUpdate(categoryDTO, categoryId)))
                .orElseThrow(() -> new NoSuchElementExistsException(String.format(NO_SUCH_ELEMENT_EXISTS_EXCEPTION, categoryId)));
    }

    @Override
    public void deleteCategory(Long categoryId) {
        categoryRepository.findById(categoryId)
                .ifPresentOrElse((category) -> categoryRepository.deleteById(categoryId),
                        () -> {
                            throw new NoSuchElementExistsException(String.format(NO_SUCH_ELEMENT_EXISTS_EXCEPTION, categoryId));
                        });
    }
}
