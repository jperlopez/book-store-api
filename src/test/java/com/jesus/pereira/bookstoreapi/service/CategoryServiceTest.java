package com.jesus.pereira.bookstoreapi.service;

import com.jesus.pereira.bookstoreapi.domain.Category;
import com.jesus.pereira.bookstoreapi.exception.NoSuchElementExistsException;
import com.jesus.pereira.bookstoreapi.mapper.CategoryMapper;
import com.jesus.pereira.bookstoreapi.repository.CategoryRepository;
import com.jesus.pereira.bookstoreapi.resource.dto.CategoryDTO;
import com.jesus.pereira.bookstoreapi.service.impl.CategoryServiceImpl;
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

    @Mock
    CategoryMapper categoryMapper;

    @InjectMocks
    CategoryServiceImpl categoryService;

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
    void givenAnIdShouldReturnCategory() {
        Long id = 1L;
        category1.setId(1L);

        given(categoryRepository.findById(id)).willReturn(Optional.of(category1));
        Category retrievedCategory = categoryService.findCategoryById(id);

        verify(categoryRepository, times(1)).findById(any());
        assertThat(retrievedCategory).isNotNull();
        assertThat(retrievedCategory).usingRecursiveComparison().isEqualTo(category1);
    }

    @Test
    void givenSearchRequestShouldReturnAllCategories() {

        List<Category> categoryList = new ArrayList<>();
        categoryList.add(category1);
        categoryList.add(category2);
        categoryList.add(category3);

        given(categoryRepository.findAll()).willReturn(categoryList);
        List<Category> retrievedAuthors = categoryService.findAllCategories();

        verify(categoryRepository, times(1)).findAll();
        assertThat(retrievedAuthors).isNotEmpty();
        assertThat(retrievedAuthors).containsAll(categoryList);
    }

    @Test
    void givenNameShouldReturnAllCategoriesWithNameLike() {

        String name = "cat";
        List<Category> categoryList = new ArrayList<>();
        categoryList.add(category1);
        categoryList.add(category2);

        given(categoryRepository.findByNameContainingIgnoreCase(name)).willReturn(categoryList);
        List<Category> retrievedCategories = categoryService.findCategoriesByNameLike(name);

        verify(categoryRepository, times(1)).findByNameContainingIgnoreCase(any());
        assertThat(retrievedCategories).isNotEmpty();
        assertThat(retrievedCategories).size().isEqualTo(2);
    }

    @Test
    void givenSaveRequestShouldSaveCategory() {

        CategoryDTO categoryDto = CategoryDTO.builder()
                .name("Category")
                .description("Test4")
                .build();

        Category categoryToPersist = Category.builder()
                .name("Category")
                .description("Test5")
                .build();


        given(categoryRepository.findByNameIgnoreCase(categoryDto.getName())).willReturn(Optional.empty());
        given(categoryMapper.toCategory(categoryDto)).willReturn(categoryToPersist);
        given(categoryRepository.save(categoryToPersist)).willAnswer(
                invocationOnMock -> invocationOnMock.getArgument(0));

        Category categoryPersisted = categoryService.createCategory(categoryDto);

        verify(categoryRepository, times(1)).save(any());
        assertThat(categoryPersisted).isNotNull();
        assertThat(categoryPersisted).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(categoryToPersist);
    }

    @Test
    void givenUpdateRequestAndIdShouldUpdateAuthor() {
        final Long id = 1L;

        CategoryDTO categoryDto = CategoryDTO.builder()
                .name("Category")
                .description("Test4")
                .build();

        Category categoryToPersist = Category.builder()
                .id(1L)
                .name("Category")
                .description("Test4")
                .build();

        given(categoryMapper.toCategoryUpdate(categoryDto, id)).willReturn(categoryToPersist);
        given(categoryRepository.findById(id)).willReturn(Optional.of(categoryToPersist));
        given(categoryRepository.save(categoryToPersist)).willReturn(categoryToPersist);

        final Category categoryUpdated = categoryService.updateCategory(categoryDto, id);

        verify(categoryRepository, times(1)).findById(any());
        verify(categoryRepository, times(1)).save(any());
        assertThat(categoryUpdated).usingRecursiveComparison().isEqualTo(categoryToPersist);
    }

    @Test
    void givenAnIdShouldDeleteAuthor() {
        final Long id = 1L;
        category2.setId(id);
        given(categoryRepository.findById(id)).willReturn(Optional.of(category1));

       categoryService.deleteCategory(id);

        verify(categoryRepository, times(1)).findById(any());
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
