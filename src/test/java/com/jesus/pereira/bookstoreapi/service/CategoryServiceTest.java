package com.jesus.pereira.bookstoreapi.service;

import com.jesus.pereira.bookstoreapi.domain.Category;
import com.jesus.pereira.bookstoreapi.exception.NoSuchElementExistsException;
import com.jesus.pereira.bookstoreapi.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    @Mock
    CategoryRepository categoryRepository;

    @InjectMocks
    CategoryService categoryService;

    private Category category1;
    private Category category2;
    private Category category3;

    @BeforeEach
    void setUp() {
        category1 = Category.builder()
                .name("Category1")
                .description("Test1")
                .build();

        category2 = Category.builder()
                .name("Category2")
                .description("Test2")
                .build();

        category3 = Category.builder()
                .name("Comedy")
                .description("Comedy category")
                .build();
    }

    @Test
    void givenAnIdShouldReturnAnAuthor() {
        Long id = 1L;
        category1.setId(1L);

        given(categoryRepository.findById(id)).willReturn(Optional.of(category1));
        Category retrievedCategory = categoryService.findCategoryById(id);

        verify(categoryRepository.findById(any()), times(1));
        assertThat(retrievedCategory).isNotNull();
        assertThat(retrievedCategory).usingRecursiveComparison().isEqualTo(category1);
    }

    @Test
    void givenSearchRequestShouldReturnAllAuthors() {

        List<Category> categoryList = new ArrayList<>();
        categoryList.add(category1);
        categoryList.add(category2);
        categoryList.add(category3);

        given(categoryRepository.findAll()).willReturn(categoryList);
        List<Category> retrievedAuthors = categoryService.findAllCategories();

        verify(categoryRepository.findAll(), times(1));
        assertThat(retrievedAuthors).isNotEmpty();
        assertThat(retrievedAuthors).containsAll(categoryList);
    }

    @Test
    void givenNameShouldReturnAuthor() {

        given(categoryRepository.findByNameIgnoreCase(category1.getName())).willReturn(Optional.of(category1));
        Category retrievedCategory = categoryService.findCategoryByName(category1.getName());

        verify(categoryRepository.findByNameIgnoreCase(any()), times(1));
        assertThat(retrievedCategory).isNotNull();
        assertThat(retrievedCategory).usingRecursiveComparison().isEqualTo(category1);
    }

    @Test
    void givenNameShouldReturnAllAuthorsWithNameLike() {

        String name = "cat";
        List<Category> categoryList = new ArrayList<>();
        categoryList.add(category1);
        categoryList.add(category2);

        given(categoryRepository.findByNameContainingIgnoreCase(name)).willReturn(categoryList);
        List<Category> retrievedCategories = categoryService.findCategoriesByNameLike(name);

        verify(categoryRepository.findAll(), times(1));
        assertThat(retrievedCategories).isNotEmpty();
        assertThat(retrievedCategories).size().isEqualTo(2);
    }

    @Test
    void givenSaveRequestShouldSaveAuthor() {
        Category categoryToPersist = Category.builder()
                .name("Category")
                .description("Test1")
                .build();

        given(categoryRepository.save(categoryToPersist)).willAnswer(
                invocationOnMock -> invocationOnMock.getArgument(0));
        Category categoryPersisted = categoryService.createCategory(categoryToPersist);

        verify(categoryRepository.save(any()), times(1));
        assertThat(categoryPersisted).isNotNull();
        assertThat(categoryPersisted).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(categoryToPersist);
    }

    @Test
    void givenUpdateRequestAndIdShouldUpdateAuthor() {
        final Long id = 1L;
        category1.setId(id);
        given(categoryRepository.findById(id)).willReturn(Optional.of(category1));

        final Category categoryUpdated = categoryService.updateCategory(category1, id);

        verify(categoryRepository.findById(any()), times(1));
        verify(categoryRepository.save(any()), times(1));
        assertThat(categoryUpdated).usingRecursiveComparison().isEqualTo(category1);
    }

    @Test
    void givenAnIdShouldDeleteAuthor() {
        final Long id = 1L;
        category2.setId(id);
        given(categoryRepository.findById(id)).willReturn(Optional.of(category1));

        categoryService.deleteCategory();

        verify(categoryRepository.findById(any()), times(1));
        verify(categoryRepository, times(1)).deleteById(1L);
    }

    @Test
    void givenAnAuthorIdNotPersistedShouldThrowException() {
        Long id = 1L;
        given(categoryRepository.findById(id)).willReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.findCategoryById(id))
                .isInstanceOf(NoSuchElementExistsException.class);
    }
}
