package com.profi_shop.services;

import com.profi_shop.exceptions.ExistException;
import com.profi_shop.exceptions.InvalidDataException;
import com.profi_shop.exceptions.SearchException;
import com.profi_shop.model.Category;
import com.profi_shop.model.Product;
import com.profi_shop.model.ProductVariation;
import com.profi_shop.model.requests.ProductCreateRequest;
import com.profi_shop.model.requests.ProductEditRequest;
import com.profi_shop.repositories.ProductRepository;
import com.profi_shop.repositories.ProductVariationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Random;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final PhotoService photoService;
    private final CategoryService categoryService;
    private final ProductVariationRepository productVariationRepository;
    private final StoreHouseService storeHouseService;

    private String generateArticul() {
        StringBuilder articul = new StringBuilder();
        Random random = new Random();
        int ARTICUL_LENGTH = 7;
        for (int i = 0; i < ARTICUL_LENGTH; i++) {
            String SYMBOLS = "0123456789abcd";
            int randomIndex = random.nextInt(SYMBOLS.length());
            char digit = SYMBOLS.charAt(randomIndex);
            articul.append(digit);
        }

        return articul.toString();
    }

    @Autowired
    public ProductService(ProductRepository productRepository, PhotoService photoService, CategoryService categoryService, ProductVariationRepository productVariationRepository, StoreHouseService storeHouseService) {
        this.productRepository = productRepository;
        this.photoService = photoService;
        this.categoryService = categoryService;
        this.productVariationRepository = productVariationRepository;
        this.storeHouseService = storeHouseService;
    }

    public void createProduct(ProductCreateRequest productToCreate) throws Exception {
        Product product = new Product();
        product.setName(productToCreate.getName());
        product.setPrime_cost(productToCreate.getPrime_cost());
        product.setPrice(productToCreate.getPrice());
        product.setDescription(productToCreate.getDescription());
        product.setCategory(productToCreate.getCategory());
        product.setBrand(productToCreate.getBrand());
        product.addTag(productToCreate.getTag());

        if (!productToCreate.getColor().equals("none")) product.setColor(productToCreate.getColor());
        else product.setColor(null);

        try {
            String firstPhoto = photoService.saveProductPhoto(productToCreate.getPhoto());
            product.getPhotos().add(firstPhoto);
        } catch (IOException e) {
            throw new InvalidDataException(InvalidDataException.INVALID_PHOTO);
        }

        try {
            productRepository.save(product);
        } catch (Exception e) {
            throw new ExistException(ExistException.PRODUCT_SKU_EXIST);
        }
        if (productToCreate.getSize() == null || productToCreate.getSize().isEmpty()) {
            addVariationToProduct(product.getId(), "Универсальный", productToCreate.getSku());
        } else {
            addVariationToProduct(product.getId(), productToCreate.getSize(), productToCreate.getSku());
        }
    }

    public Page<Product> productsByActiveStocks(int page, int sort) {
        Pageable pageable;
        if (sort != 0) {
            if (sort == 1) pageable = PageRequest.of(page, 9, Sort.by(Sort.Direction.DESC, "create_date"));
            else if (sort == 2) pageable = PageRequest.of(page, 9, Sort.by(Sort.Direction.ASC, "price"));
            else if (sort == 3) pageable = PageRequest.of(page, 9, Sort.by(Sort.Direction.DESC, "price"));
            else pageable = PageRequest.of(page, 9, Sort.by(Sort.Direction.DESC, "name"));
        } else {
            pageable = PageRequest.of(page, 9);
        }

        return productRepository.findProductsInActiveStocks(pageable);
    }

    public Page<Product> productsFilteredPage(int page, Long categoryId, String size, String query, int minPrice, int maxPrice, int sort, String tag, String brand) {
        Pageable pageable = switch (sort) {
            case 0 -> PageRequest.of(page, 9, Sort.by(Sort.Direction.DESC, "create_date"));
            case 1 -> PageRequest.of(page, 9, Sort.by(Sort.Direction.ASC, "price"));
            case 2 -> PageRequest.of(page, 9, Sort.by(Sort.Direction.DESC, "price"));
            default -> PageRequest.of(page, 9, Sort.by(Sort.Direction.ASC, "name"));
        };

        String targetQuery = (query.equals("")) ? null : query;
        Integer targetMinPrice = (minPrice == maxPrice) ? null : minPrice;
        Integer targetMaxPrice = (maxPrice == minPrice) ? null : maxPrice;
        Category category = (categoryId == 0) ? null : categoryService.getCategoryById(categoryId);

        if (category == null)
            return productRepository.findAllByFiltersWithoutCategory(targetQuery, targetMinPrice, targetMaxPrice, size, tag, brand, pageable);
        return productRepository.findAllByFilters(category, targetQuery, targetMinPrice, targetMaxPrice, size, tag, brand, pageable);
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
        return productRepository.findByNameLike(request, PageRequest.of(page, 9));
    }

    public void addPhotoToProductById(Long productId, MultipartFile photo) throws IOException {
        Product product = getProductById(productId);
        String newPhoto = photoService.saveProductPhoto(photo);
        product.addPhoto(newPhoto);
        productRepository.save(product);
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

    public void addVariationToProduct(Long productId, String size, String sku) throws ExistException {
        try {
            Product product = getProductById(productId);
            ProductVariation productVariation = new ProductVariation();
            productVariation.setSize(size);
            productVariation.setParent(product);
            if (sku != null && !sku.isEmpty()) {
                productVariation.setSku(sku);
                productVariationRepository.save(productVariation);
            } else {
                productVariationRepository.save(productVariation);
                productVariation.setSku(generateArticul());
                productVariationRepository.save(productVariation);
            }
            storeHouseService.createStoreHouseProduct(productVariation);
        } catch (DataIntegrityViolationException e) {
            if (e.getRootCause().getMessage().contains("sku"))
                throw new ExistException(ExistException.PRODUCT_SKU_EXIST);
            throw new ExistException(ExistException.SIZE_OF_PRODUCT_EXIST);
        }
    }

    public void addSpecificationToProduct(Long productId, String key, String value) throws ExistException {
        try {

            Product product = getProductById(productId);
            product.addSpecification(key, value);
            productRepository.save(product);
        } catch (Exception e) {
            throw new ExistException(ExistException.SPECIFICATION_EXIST);
        }
    }

    public void deleteSpecificationOfProduct(Long productId, String key) {
        Product product = getProductById(productId);
        product.removeSpecification(key);
        productRepository.save(product);
    }

    public List<String> getTags() {
        return productRepository.findTop10DistinctTags();
    }

    public List<String> getSizes() {
        return productVariationRepository.findTop10DistinctSize();
    }

    public List<String> getBrands() {
        return productRepository.findTop10DistinctBrands();
    }

    public void productEdit(ProductEditRequest productEditRequest) {
        Product product = getProductById(productEditRequest.getTargetProductId());

        if (productEditRequest.getPrimeCost() != null && productEditRequest.getPrimeCost() > 0)
            product.setPrime_cost(productEditRequest.getPrimeCost());
        if (productEditRequest.getPrice() != null && productEditRequest.getPrice() > 0)
            product.setPrice(productEditRequest.getPrice());
        if (productEditRequest.getName() != null && !productEditRequest.getName().equals(""))
            product.setName(productEditRequest.getName());
        if (productEditRequest.getBrand() != null && !productEditRequest.getBrand().equals(""))
            product.setBrand(productEditRequest.getBrand());
        if (productEditRequest.getColor() != null && !productEditRequest.getColor().equals(""))
            product.setColor(productEditRequest.getColor());
        if (product.getCategory() == null || !productEditRequest.getCategoryId().equals(product.getCategory().getId())) {
            Category category = categoryService.getCategoryById(productEditRequest.getCategoryId());
            product.setCategory(category);
        }
        productRepository.save(product);
    }

    public List<ProductVariation> getProductVariations(Long productId) {
        Product product = getProductById(productId);
        return productVariationRepository.findByParent(product);
    }

    public void deleteProduct(Long productId) throws Exception {
        Product product = getProductById(productId);
        for(String photo : product.getPhotos()){
            try{
                photoService.deletePhoto(photo);
            }catch (Exception ignore){}
        }
        try{
            productRepository.delete(product);
        }catch (Exception e){
            throw new Exception("Ошибка при удалении товара");
        }
    }
}
