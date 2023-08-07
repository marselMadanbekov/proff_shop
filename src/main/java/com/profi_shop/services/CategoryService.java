package com.profi_shop.services;

import com.profi_shop.model.Category;
import com.profi_shop.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category createCategory(String name) {
        Category category = new Category();
        category.setName(name);
        return categoryRepository.save(category);
    }

    public List<Category> getAllCategories(){
        return categoryRepository.findAll();
    }

    public void deleteCategory(Long categoryId) {
        Category category = getCategoryById(categoryId);
        System.out.println("deleting category" + category.getName());
        categoryRepository.delete(category);
    }

    public Page<Category> getPagedCategories(Integer page) {
        Pageable pageable = PageRequest.of(page, 15, Sort.by(Sort.Direction.ASC,"name"));
        return categoryRepository.findByParentIsNull(pageable);
    }

    public Category getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId).orElse(null);
    }

    public void createSubcategory(Long superCat, String subcategoryName) {
        Category superCategory = getCategoryById(superCat);
        Category sub = createCategory(subcategoryName);

        sub.setParent(superCategory);
        superCategory.addSubCategory(sub);
        categoryRepository.save(sub);
        categoryRepository.save(superCategory);
    }

    public List<Category> getMainCategories() {
        return categoryRepository.findByParentIsNull();
    }
}
