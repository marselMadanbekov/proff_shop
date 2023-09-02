package com.profi_shop.controllers.adminControllers;

import com.profi_shop.dto.ProductDTO;
import com.profi_shop.dto.ProductDetailsDTO;
import com.profi_shop.model.*;
import com.profi_shop.model.requests.ProductCreateRequest;
import com.profi_shop.model.requests.ProductEditRequest;
import com.profi_shop.services.*;
import com.profi_shop.services.facade.ProductFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
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
    private final ProductsAnalytics productsAnalytics;
    private final StoreService storeService;


    @Autowired
    public ProductController(ProductService productService, CategoryService categoryService, ProductFacade productFacade, StoreHouseService stockService, ProductsAnalytics productsAnalytics, StoreService storeService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.productFacade = productFacade;
        this.storeHouseService = stockService;
        this.productsAnalytics = productsAnalytics;
        this.storeService = storeService;
    }

    @GetMapping("/products")
    public String products(@RequestParam(value = "categoryId", required = false) Optional<Long> categoryId,
                           @RequestParam(value = "size", required = false) Optional<String> size,
                           @RequestParam(value = "tag", required = false) Optional<String> tag,
                           @RequestParam(value = "brand", required = false) Optional<String> brand,
                           @RequestParam(value = "minPrice", required = false) Optional<Integer> minPrice,
                           @RequestParam(value = "maxPrice", required = false) Optional<Integer> maxPrice,
                           @RequestParam(value = "query", required = false) Optional<String> query,
                           @RequestParam(value = "page", required = false) Optional<Integer> page,
                           @RequestParam(value = "sort", required = false) Optional<Integer> sort,
                           Model model) {
        List<Store> stores = storeService.getAllStores();
        List<Category> categories = categoryService.getAllCategories();
        Page<ProductDetailsDTO> products = productFacade.mapToProductDetailsDTOPage(productService.productsFilteredPage(page.orElse(0),
                categoryId.orElse(0L),
                size.orElse(null),
                query.orElse(""),
                minPrice.orElse(0),
                maxPrice.orElse(0),
                sort.orElse(0),
                tag.orElse(null),
                brand.orElse(null)));


        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        model.addAttribute("stores", stores);

        model.addAttribute("sortType", sort.orElse(0));
        model.addAttribute("minPrice", minPrice.orElse(0));
        model.addAttribute("maxPrice", maxPrice.orElse(0));
        model.addAttribute("selectedCategory", categoryId.orElse(0L));
        model.addAttribute("selectedSize", size.orElse(""));
        model.addAttribute("query", query.orElse(""));
        return "admin/product/products";
    }

    @PostMapping("/create-product")
    public ResponseEntity<Map<String, String>> createProduct(@ModelAttribute ProductCreateRequest product) {
        Map<String, String> response = new HashMap<>();
        try {
            productService.createProduct(product);
            response.put("message", "Продукт успешно создан");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/product/addSpecification")
    public ResponseEntity<Map<String, String>> addSpecification(@RequestParam String key,
                                                                @RequestParam String value,
                                                                @RequestParam Long productId) {
        Map<String, String> response = new HashMap<>();
        try {
            productService.addSpecificationToProduct(productId, key, value);
            response.put("message", "Спецификация успешно добавлена");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/product/deleteSpecification")
    public ResponseEntity<Map<String, String>> deleteSpecification(@RequestParam String specKey,
                                                                   @RequestParam Long productId) {
        Map<String, String> response = new HashMap<>();
        System.out.println(productId + " product id \n + key " + specKey);
        try {
            productService.deleteSpecificationOfProduct(productId, specKey);
            response.put("message", "Спецификация успешно удалена");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/products/deleteVariation")
    public ResponseEntity<Map<String, String>> deleteVariation(@RequestParam Long variationId) {
        Map<String, String> response = new HashMap<>();
        try {
            response.put("message", "Размер товара успешно удален");
            productService.deleteProductVariationById(variationId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/products/addVariation")
    public ResponseEntity<Map<String, String>> addVariation(@RequestParam String size,
                                                            @RequestParam Long productId,
                                                            @RequestParam(value = "sku", required = false) Optional<String> sku) {
        Map<String, String> response = new HashMap<>();
        try {
            productService.addVariationToProduct(productId, size, sku.orElse(null));
            response.put("message", "Новый размер товара успешно добавлен");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("/receive-product")
    public ResponseEntity<Map<String, String>> receiveProduct(@RequestParam("store") Long storeId,
                                                              @RequestParam("productId") Long productVar,
                                                              @RequestParam("quantity") int quantity,
                                                              Principal principal) {
        Map<String, String> response = new HashMap<>();
        try {
            storeHouseService.quantityUp(storeId, productVar, quantity,principal.getName());
            response.put("message", "Поступление сохранено");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/addPhoto")
    public ResponseEntity<Map<String, String>> addPhoto(@RequestParam("photo") MultipartFile photo,
                                                        @RequestParam("productId") Long productId) {
        Map<String, String> response = new HashMap<>();
        try {
            productService.addPhotoToProductById(productId, photo);
            response.put("message", "Фото успешно добавлено!");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("/deletePhoto")
    public ResponseEntity<Map<String, String>> deletePhoto(@RequestParam("photo") String photo,
                                                           @RequestParam("productId") Long productId) {
        Map<String, String> response = new HashMap<>();
        try {
            productService.deletePhotoByProductId(photo, productId);
            response.put("message", "Фото успешно уделено");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/productDetails")
    public String productDetails(@RequestParam("productId") Long productId, Model model) {
        ProductDetailsDTO product = productFacade.productToProductDetailsDTO(productService.getProductById(productId));
        List<Store> stores = storeService.getAllStores();
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("product", product);
        model.addAttribute("categories", categories);
        model.addAttribute("stores", stores);
        return "admin/product/productDetails";
    }

    @PostMapping("/editProduct")
    public ResponseEntity<Map<String, String>> editProduct(@ModelAttribute ProductEditRequest productEditRequest) {
        Map<String, String> response = new HashMap<>();
        try {
            System.out.println(productEditRequest.getTargetProductId() + " -> productId");
            System.out.println(productEditRequest.getCategoryId() + " -> category");
            productService.productEdit(productEditRequest);
            response.put("message", "Изменения успешно сохранены");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
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
    public ResponseEntity<Object> filterProducts(@RequestParam(value = "size", required = false) Optional<String> size,
                                                 @RequestParam(value = "categoryId", required = false) Optional<Long> categoryId,
                                                 @RequestParam(value = "minPrice", required = false) Optional<Integer> minPrice,
                                                 @RequestParam(value = "maxPrice", required = false) Optional<Integer> maxPrice,
                                                 @RequestParam(value = "query", required = false) Optional<String> query,
                                                 @RequestParam(value = "tag", required = false) Optional<String> tag,
                                                 @RequestParam(value = "brand", required = false) Optional<String> brand,
                                                 @RequestParam(value = "page", required = false) Optional<Integer> page,
                                                 @RequestParam(value = "sort", required = false) Optional<Integer> sort) {
        try {

            Page<ProductDTO> products = productFacade.mapToProductDTOPage(
                    productService.productsFilteredPage(page.orElse(0),
                            categoryId.orElse(0L),
                            size.orElse(null),
                            query.orElse(""),
                            minPrice.orElse(0),
                            maxPrice.orElse(0),
                            sort.orElse(0),
                            tag.orElse(null),
                            brand.orElse(null))
            );
            return new ResponseEntity<>(products, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("ERROR", HttpStatus.BAD_REQUEST);
        }
    }
}
