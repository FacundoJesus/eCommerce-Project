package com.ecommerce.project.service;

import com.ecommerce.project.model.Category;
import com.ecommerce.project.repositories.iCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService implements iCategoryService {

    @Autowired
    private iCategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public void createCategory(Category category) {
        categoryRepository.save(category);
    }

    @Override
    public String deleteCategory(Long categoryId) {

        Optional<Category> savedCategoryOptional = categoryRepository.findById(categoryId);

        if (savedCategoryOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Resource not found");
        }
        categoryRepository.deleteById(categoryId);
        return "Category with categoryId: " + categoryId + " deleted succesfully!";
    }

    @Override
    public Category updateCategory(Category category, Long categoryId) {

        Optional<Category> savedCategoryOptional = categoryRepository.findById(categoryId);
        Category savedCategory = savedCategoryOptional
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Resource not found"));

        category.setCategoryId(categoryId);
        savedCategory = categoryRepository.save(category);
        return savedCategory;
    }


}
