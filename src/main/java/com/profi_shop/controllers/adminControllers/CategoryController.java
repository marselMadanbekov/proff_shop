package com.profi_shop.controllers.adminControllers;

import com.profi_shop.model.Category;
import com.profi_shop.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/create-category")
    public String createCategory(@RequestParam("name") String name) {
        try {
            categoryService.createCategory(name);
            return "redirect:/admin/categories";
        } catch (Exception e) {
            return "redirect:/"; // Перенаправьте пользователя на нужную страницу после успешного создания продукта
        }
    }

    @GetMapping("/categoriesByPage")
    public ResponseEntity<Page<Category>> getCategoriesByPage(@RequestParam("page") Integer page) {
        try {
            System.out.println("hello pagination");
            Page<Category> categories = categoryService.getPagedCategories(page);
            return new ResponseEntity<>(categories, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/categories")
    public String categories(@RequestParam(value = "page", required = false) Optional<Integer> page,
                             Model model) {

        Page<Category> categories = categoryService.getPagedCategories(page.orElse(0));
        for (Category category : categories) {
            System.out.println(category.getId());
            System.out.println(category.getName());
        }
        model.addAttribute("categories", categories);
        return "admin/category/categories";
    }

    @PostMapping("/categories/delete/{categoryId}")
    public String deleteCategory(@PathVariable("categoryId") Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return "admin/category/categories";
    }
}
