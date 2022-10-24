package com.jesus.pereira.bookstoreapi.repository;

import com.jesus.pereira.bookstoreapi.domain.Category;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class CategoryRepositoryTest {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    TestEntityManager entityManager;

    private Category category1;
    private Category category2;

    @BeforeEach
    void setUp(){
        category1 = Category.builder()
                .name("Category1")
                .description("Test category 1")
                .build();

        category2 = Category.builder()
                .name("Category2")
                .description("Test category 2")
                .build();
    }

    @AfterEach
    void tearDown(){
        categoryRepository.deleteAll();
    }

    @Test
    void shouldReturnCategory(){
        Category categoryPersisted = entityManager.persistAndFlush(category1);
        Category categoryRetrieved = categoryRepository.findById(categoryPersisted.getId()).orElse(null);

        assertThat(categoryRetrieved).isNotNull();
        assertThat(categoryRetrieved).usingRecursiveComparison().isEqualTo(categoryPersisted);
    }

    @Test
    void shouldReturnAllCategories(){
        List<Category> categoryList = new ArrayList<>();
        categoryList.add(category1);
        categoryList.add(category2);

        entityManager.persistAndFlush(category1);
        entityManager.persistAndFlush(category2);

        List<Category> categoriesRetrieved = categoryRepository.findAll();

        assertThat(categoriesRetrieved).size().isEqualTo(2);
        assertThat(categoriesRetrieved).containsAll(categoryList);
    }

    @Test
    void shouldReturnCategoryByName(){
        Category categoryPersisted = entityManager.persistAndFlush(category1);
        Category categoryRetrieved = categoryRepository.findByNameIgnoreCase("Category1").orElse(null);

        assertThat(categoryRetrieved).isNotNull();
        assertThat(categoryRetrieved).usingRecursiveComparison().isEqualTo(categoryPersisted);

    }

    @Test
    void shouldReturnAllCategoriesByNameLike(){
        Category category3 = Category.builder()
                .name("Comedy")
                .description("Comedy category")
                .build();

        List<Category> categoryList = new ArrayList<>();
        categoryList.add(category1);
        categoryList.add(category2);
        categoryList.add(category3);

        entityManager.persistAndFlush(category1);
        entityManager.persistAndFlush(category2);
        entityManager.persistAndFlush(category3);

        List<Category> categoriesRetrieved = categoryRepository.findAll();
        List<Category> categoriesRetrievedByName = categoryRepository.findByNameContainingIgnoreCase("cat");

        assertThat(categoriesRetrieved).size().isEqualTo(3);
        assertThat(categoriesRetrievedByName).size().isEqualTo(2);


    }

    @Test
    void shouldDeleteAnAuthor(){
        Long id = entityManager.persistAndFlush(category1).getId();
        entityManager.persistAndFlush(category2);
        categoryRepository.deleteById(id);

        List<Category> categoryList = categoryRepository.findAll();

        assertThat(categoryList).size().isEqualTo(1);
        assertThat(categoryList).contains(category2);
    }
}
