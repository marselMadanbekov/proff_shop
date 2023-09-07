package com.profi_shop.controllers.adminControllers;

import com.profi_shop.dto.CategoryDTO;
import com.profi_shop.model.Category;
import com.profi_shop.services.CategoryService;
import com.profi_shop.services.facade.CategoryFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class CategoryController {

    private final CategoryService categoryService;

    private final CategoryFacade categoryFacade;
    @Autowired
    public CategoryController(CategoryService categoryService, CategoryFacade categoryFacade) {
        this.categoryService = categoryService;
        this.categoryFacade = categoryFacade;
    }

    @PostMapping("/create-category")
    public ResponseEntity<Map<String,String>> createCategory(@RequestParam("name") String name) {
        Map<String,String> response = new HashMap<>();
        try {
            categoryService.createCategory(name);
            response.put("message", "Категория успешно создана");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); // Перенаправьте пользователя на нужную страницу после успешного создания продукта
        }
    }
    @PostMapping("/create-subcategory")
    public ResponseEntity<Map<String,String>> createSubCategory(@RequestParam("superCategory") Long superCategory,
                                    @RequestParam("subcategoryName") String subcategoryName) {
        Map<String,String> response = new HashMap<>();
        try {
            categoryService.createSubcategory(superCategory,subcategoryName);
            response.put("message", "Категория успешно создана!");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/categoryDetails")
    public String categoryDetails(@RequestParam("categoryId") Long categoryId,
                                  Model model){
        Category category = categoryService.getCategoryById(categoryId);
        model.addAttribute("category", category);
        return "admin/category/categoryDetails";
    }

    @GetMapping("/categoriesByPage")
    public ResponseEntity<Page<CategoryDTO>> getCategoriesByPage(@RequestParam("page") Integer page) {
        try {
            System.out.println("hello pagination");
            Page<CategoryDTO> categories = categoryFacade.mapToProductDTOPage(categoryService.getPagedCategories(page));
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
            System.out.println(category.getName());
            for (Category sub : category.getSubCategories()){
                System.out.println(" -> " + sub.getName());
            }
        }
        model.addAttribute("categories", categories);
        return "admin/category/categories";
    }

    @PostMapping("/categories/delete")
    public ResponseEntity<Map<String,String>> deleteCategory(@RequestParam("categoryId") Long categoryId) {
        Map<String,String> response = new HashMap<>();
        try {
            System.out.println("deleting category " + categoryId);
            categoryService.deleteCategory(categoryId);
            response.put("message", "Категория успешно удалена");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}
