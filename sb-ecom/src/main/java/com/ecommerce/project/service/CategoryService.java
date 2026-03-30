package com.ecommerce.project.service;

import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.repositories.iCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService implements iCategoryService {

    @Autowired
    private iCategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        if(categories.isEmpty()) {
            throw new APIException("No category created till now.");
        }
        return categories;
    }

    @Override
    public void createCategory(Category category) {

        Category savedCategory = categoryRepository.findByCategoryName(category.getCategoryName());

        if(savedCategory != null) {
            throw new APIException("Category with the name: " + category.getCategoryName() + " already exists!");
        }
        categoryRepository.save(category);

    }

    @Override
    public String deleteCategory(Long categoryId) {

        Optional<Category> savedCategoryOptional = categoryRepository.findById(categoryId);

        if (savedCategoryOptional.isEmpty()) {
            throw new ResourceNotFoundException("Category","categoryId",categoryId);
        }
        categoryRepository.deleteById(categoryId);
        return "Category with categoryId: " + categoryId + " deleted succesfully!";
    }

    @Override
    public Category updateCategory(Category category, Long categoryId) {

        Optional<Category> savedCategoryOptional = categoryRepository.findById(categoryId);
        Category savedCategory = savedCategoryOptional
                .orElseThrow(() -> new ResourceNotFoundException("Category","categoryId",categoryId));

        category.setCategoryId(categoryId);
        savedCategory = categoryRepository.save(category);
        return savedCategory;
    }


}
