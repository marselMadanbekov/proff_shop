package com.profi_shop.controllers.adminControllers;

import com.profi_shop.model.Category;
import com.profi_shop.model.Product;
import com.profi_shop.model.ProductGroup;
import com.profi_shop.model.requests.ProductGroupRequest;
import com.profi_shop.services.CategoryService;
import com.profi_shop.services.ProductGroupService;
import com.profi_shop.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class ProductGroupController {

    private final ProductGroupService productGroupService;
    private final ProductService productService;
    private final CategoryService categoryService;

    @Autowired
    public ProductGroupController(ProductGroupService productGroupService, ProductService productService, CategoryService categoryService) {
        this.productGroupService = productGroupService;
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping("/productGroupDetails")
    public String productGroupDetails(@RequestParam("groupId") Long groupId,
                                      Model model){
        ProductGroup productGroup = productGroupService.getProductGroupById(groupId);
        Page<Product> products = productService.getPagedProducts(0,10);
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("productGroup", productGroup);
        model.addAttribute("products", products);
        return "admin/product/productGroupDetails";
    }
    @GetMapping("/productGroups")
    public String productGroups(@RequestParam(value = "page",required = false) Optional<Integer> page,
                                @RequestParam(value = "name", required = false) Optional<String> name,
                                @RequestParam(value = "sort", required = false) Optional<Integer> sort,
                                Model model){
        Page<ProductGroup> productGroups = productGroupService.productGroupsFiltered(page.orElse(0),name.orElse(null), sort.orElse(0));
        model.addAttribute("query", name.orElse(null));
        model.addAttribute("selectedSort", sort.orElse(0));
        model.addAttribute("productGroups", productGroups);
        return "admin/product/productGroups";
    }
    @GetMapping("/create-productGroup")
    public String createProductGroup(Model model){
        Page<Product> products = productService.getPagedProducts(0, 10);
        model.addAttribute("products", products);
        return "admin/product/createProductGroup";
    }

    @PostMapping("/create-productGroup")
    public ResponseEntity<Map<String,String>> createProductGroup(@RequestBody ProductGroupRequest productGroupRequest) {
        Map<String, String> response = new HashMap<>();
        try {
            productGroupService.createProductGroup(productGroupRequest);
            response.put("message", "Карточка успешно создана");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }
    @PostMapping("/products/removeFromGroup")
    public ResponseEntity<Map<String,String>> removeFromGroup(@RequestParam Long groupId,
                                                              @RequestParam Long productId){
        Map<String,String> response = new HashMap<>();
        try{
            productGroupService.removeProductFromGroup(productId, groupId);
            response.put("message", "Товар успешно удален из карточки");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/products/addToGroup")
    public ResponseEntity<Map<String, String>> addToGroup(@RequestParam Long groupId,
                                                          @RequestParam Long productId){
        Map<String, String> response = new HashMap<>();
        try{
            productGroupService.addProductToGroup(productId, groupId);
            response.put("message", "Товар успешно добален в карточку");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/products/deleteGroup")
    public ResponseEntity<Map<String,String>> deleteProductGroup(@RequestParam Long groupId){
        Map<String,String> response = new HashMap<>();
        try{
            productGroupService.deleteGroupById(groupId);
            response.put("message", "Карточка успешно удалена");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}
