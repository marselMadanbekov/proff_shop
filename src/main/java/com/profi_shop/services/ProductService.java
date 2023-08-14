package com.profi_shop.services;

import com.profi_shop.exceptions.ExistException;
import com.profi_shop.exceptions.InvalidDataException;
import com.profi_shop.exceptions.SearchException;
import com.profi_shop.model.Category;
import com.profi_shop.model.Product;
import com.profi_shop.model.ProductVariation;
import com.profi_shop.model.enums.ProductSize;
import com.profi_shop.model.requests.ProductCreateRequest;
import com.profi_shop.repositories.ProductRepository;
import com.profi_shop.repositories.ProductVariationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final PhotoService photoService;
    private final CategoryService categoryService;
    private final ProductVariationRepository productVariationRepository;
    private final StoreHouseService storeHouseService;
    private final StockService stockService;

    @Autowired
    public ProductService(ProductRepository productRepository, PhotoService photoService, CategoryService categoryService, ProductVariationRepository productVariationRepository, StoreHouseService storeHouseService, StockService stockService) {
        this.productRepository = productRepository;
        this.photoService = photoService;
        this.categoryService = categoryService;
        this.productVariationRepository = productVariationRepository;
        this.storeHouseService = storeHouseService;
        this.stockService = stockService;
    }

    public void createProduct(ProductCreateRequest productToCreate) throws Exception {
        Product product = new Product();
        product.setName(productToCreate.getName());
        product.setPrime_cost(productToCreate.getPrime_cost());
        product.setPrice(productToCreate.getPrice());
        product.setSku(productToCreate.getSku());
        product.setDescription(productToCreate.getDescription());
        product.setCategory(productToCreate.getCategory());

        if (!productToCreate.getColor().equals("none")) product.setColor(productToCreate.getColor());
        else product.setColor(null);


        try {
            String firstPhoto = photoService.savePhoto(productToCreate.getPhoto());
            product.getPhotos().add(firstPhoto);
        } catch (IOException e) {
            throw new InvalidDataException(InvalidDataException.INVALID_PHOTO);
        }

        productRepository.save(product);

        ProductVariation productVariation = new ProductVariation();
        productVariation.setProductSize(ProductSize.values()[productToCreate.getSize()]);
        productVariation.setParent(product);
        productVariationRepository.save(productVariation);
    }

    public Page<Product> productsFilteredPage(int page, Long categoryId, int size, String query, int minPrice, int maxPrice, int sort) {
        Pageable pageable = null;
        if (sort != 0) {
            if (sort == 1) pageable = PageRequest.of(page, 9, Sort.by(Sort.Direction.DESC, "create_date"));
            else if (sort == 2) pageable = PageRequest.of(page, 9, Sort.by(Sort.Direction.ASC, "price"));
            else if (sort == 3) pageable = PageRequest.of(page, 9, Sort.by(Sort.Direction.DESC, "price"));
            else pageable = PageRequest.of(page, 9, Sort.by(Sort.Direction.DESC, "name"));
        } else {
            pageable = PageRequest.of(page, 9);
        }
        Category category = (categoryId == 0) ? null : categoryService.getCategoryById(categoryId);
        ProductSize targetSize = (size == 0) ? null : ProductSize.values()[size];
        String targetQuery = (query.equals("")) ? null : query;

        Integer targetMinPrice = (minPrice == maxPrice) ? null : minPrice;
        Integer targetMaxPrice = (maxPrice == minPrice) ? null : maxPrice;

        return productRepository.findAllByFilters(category, targetQuery, targetMinPrice, targetMaxPrice, pageable);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Page<Product> getPagedProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findAll(pageable);
    }

    public Product getProductById(Long productId) {
        return productRepository.findById(productId).orElseThrow(() -> new SearchException(SearchException.PRODUCT_NOT_FOUND));
    }


    public Page<Product> search(String request, int page) {
        if (request.equals("")) {
            return productRepository.findAll(PageRequest.of(page, 9));
        }
        return productRepository.findAllByName(request, PageRequest.of(page, 9));
    }

    public Product addPhotoToProductById(Long productId, MultipartFile photo) throws IOException {
        Product product = getProductById(productId);
        String newPhoto = photoService.savePhoto(photo);
        product.addPhoto(newPhoto);
        return productRepository.save(product);
    }

    public void deletePhotoByProductId(String photo, Long productId) throws IOException {
        Product product = getProductById(productId);
        product.removePhoto(photo);
        photoService.deletePhoto(photo);
        productRepository.save(product);
    }

    public void deleteProductVariationById(Long variationId) {
        productVariationRepository.delete(getProductVariationById(variationId));
    }

    private ProductVariation getProductVariationById(Long variationId) {
        return productVariationRepository.findById(variationId).orElseThrow(() -> new SearchException(SearchException.PRODUCT_VARIATION_NOT_FOUND));
    }

    public void addVariationToProduct(Long productId, Integer size) throws ExistException {
        try {
            Product product = getProductById(productId);
            ProductVariation productVariation = new ProductVariation();
            productVariation.setProductSize(ProductSize.values()[size]);
            productVariation.setParent(product);
            productVariationRepository.save(productVariation);
            storeHouseService.createStoreHouseProduct(productVariation);
        } catch (Exception e){
            throw new ExistException(ExistException.SIZE_OF_PRODUCT_EXIST);
        }
    }
}
