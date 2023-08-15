package com.profi_shop.controllers.adminControllers;

import com.profi_shop.dto.ProductDTO;
import com.profi_shop.dto.ProductDetailsDTO;
import com.profi_shop.exceptions.ExistException;
import com.profi_shop.model.*;
import com.profi_shop.model.requests.ProductCreateRequest;
import com.profi_shop.model.requests.ProductGroupRequest;
import com.profi_shop.services.*;
import com.profi_shop.services.facade.ProductFacade;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class ProductController {
    @Value("${uploads_path}")
    private String uploadDir;
    private final ProductService productService;
    private final CategoryService categoryService;

    private final ProductFacade productFacade;
    private final StoreHouseService storeHouseService;
    private final StoreService storeService;

    private final ProductGroupService productGroupService;

    public ProductController(ProductService productService, CategoryService categoryService, ProductFacade productFacade, StoreHouseService stockService, StoreService storeService, ProductGroupService productGroupService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.productFacade = productFacade;
        this.storeHouseService = stockService;
        this.storeService = storeService;
        this.productGroupService = productGroupService;
    }


    @PostMapping("/create-product")
    public ResponseEntity<String> createProduct(@ModelAttribute ProductCreateRequest product) {
        try {
            productService.createProduct(product);
            return new ResponseEntity<>("Successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/products/deleteVariation")
    public ResponseEntity<Map<String, String>> deleteVariation(@RequestParam Long variationId) {
        Map<String,String> response = new HashMap<>();
        try{
            response.put("message", "Размер товара успешно удален");
            productService.deleteProductVariationById(variationId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/products/addVariation")
    public ResponseEntity<Map<String, String>> addVariation(@RequestParam Integer size,
                                                            @RequestParam Long productId) {
        Map<String,String> response = new HashMap<>();
        try{
            response.put("message", "Новый размер товара успешно добавлен");
            productService.addVariationToProduct(productId, size);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/products")
    public String products(@RequestParam(value = "categoryId", required = false) Optional<Long> categoryId,
                           @RequestParam(value = "size", required = false) Optional<Integer> size,
                           @RequestParam(value = "minPrice", required = false) Optional<Integer> minPrice,
                           @RequestParam(value = "maxPrice", required = false) Optional<Integer> maxPrice,
                           @RequestParam(value = "query", required = false) Optional<String> query,
                           @RequestParam(value = "page", required = false) Optional<Integer> page,
                           @RequestParam(value = "sort", required = false) Optional<Integer> sort,
                           Model model) {
        List<Store> stores = storeService.getAllStores();
        List<Category> categories = categoryService.getAllCategories();
        Page<ProductDetailsDTO> products = productFacade.mapToProductDetailsDTOPage(productService.productsFilteredPage(page.orElse(0), categoryId.orElse(0L), size.orElse(0), query.orElse(""), minPrice.orElse(0), maxPrice.orElse(0), sort.orElse(0)));


        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        model.addAttribute("stores", stores);

        model.addAttribute("sortType", sort.orElse(0));
        model.addAttribute("minPrice", minPrice.orElse(0));
        model.addAttribute("maxPrice", maxPrice.orElse(0));
        model.addAttribute("selectedCategory", categoryId.orElse(0L));
        model.addAttribute("selectedSize", size.orElse(0));
        model.addAttribute("query", query.orElse(""));
        return "admin/product/products";
    }

    @PostMapping("/receive-product")
    public ResponseEntity<Map<String ,String>> receiveProduct(@RequestParam("store") Long storeId,
                                              @RequestParam("productId") Long productVar,
                                              @RequestParam("quantity") int quantity) {
        Map<String,String> response = new HashMap<>();
        try {
            storeHouseService.quantityUp(storeId, productVar, quantity);
            response.put("message", "Поступление сохранено");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/addPhoto")
    public String addPhoto(@RequestParam("photo") MultipartFile photo,
                           @RequestParam("productId") Long productId) {
        try {
            Product product = productService.addPhotoToProductById(productId, photo);
            return "redirect:/admin/productDetails?productId=" + product.getId();
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @GetMapping("/productGroupDetails")
    public String productGroupDetails(@RequestParam("groupId") Long groupId,
                                      Model model){
        ProductGroup productGroup = productGroupService.getProductGroupById(groupId);
        Page<Product> products = productService.getPagedProducts(0,10);
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

    @GetMapping("/deletePhoto")
    public String deletePhoto(@RequestParam("photo") String photo,
                              @RequestParam("productId") Long productId) {
        try {
            productService.deletePhotoByProductId(photo, productId);
            return "redirect:/productDetails?productId=" + productId;
        } catch (Exception e) {
            return e.getMessage();
        }

    }

    @GetMapping("/productDetails")
    public String productDetails(@RequestParam("productId") Long productId, Model model) {
        ProductDetailsDTO product = productFacade.productToProductDetailsDTO(productService.getProductById(productId));
        List<Store> stores = storeService.getAllStores();
        model.addAttribute("product", product);
        model.addAttribute("stores", stores);
        return "admin/product/productDetails";
    }

    @GetMapping("/createProduct")
    public String createProduct(Model model) {
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        return "admin/product/createProduct";
    }

    @GetMapping("/products/search")
    public ResponseEntity<Page<Product>> searchProduct(@RequestParam(value = "page", required = false) Optional<Integer> page,
                                                       @RequestParam("query") String query) {
        Page<Product> products = productService.search(query, page.orElse(0));
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/productsByPage")
    public ResponseEntity<Page<Product>> productPage(@RequestParam("page") Integer page) {
        Page<Product> products = productService.getPagedProducts(page, 10);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/filter")
    public ResponseEntity<Object> filterProducts(@RequestParam(value = "size", required = false) Optional<Integer> size,
                                                  @RequestParam(value = "minPrice", required = false) Optional<Integer> minPrice,
                                                  @RequestParam(value = "maxPrice", required = false) Optional<Integer> maxPrice,
                                                  @RequestParam(value = "query", required = false) Optional<String> query,
                                                  @RequestParam(value = "page", required = false) Optional<Integer> page,
                                                  @RequestParam(value = "sort", required = false) Optional<Integer> sort) {
        try {

            Page<ProductDTO> products = productFacade.mapToProductDTOPage(productService.productsFilteredPage(page.orElse(0), 0L, size.orElse(0), query.orElse(""), minPrice.orElse(0), maxPrice.orElse(0), sort.orElse(0)));
            return new ResponseEntity<>(products, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>("ERROR", HttpStatus.BAD_REQUEST);
        }
    }
}
