package com.profi_shop.services;

import com.profi_shop.exceptions.SearchException;
import com.profi_shop.model.Product;
import com.profi_shop.model.enums.ProductSize;
import com.profi_shop.model.requests.ProductCreateRequest;
import com.profi_shop.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final PhotoService photoService;

    @Autowired
    public ProductService(ProductRepository productRepository, PhotoService photoService) {
        this.productRepository = productRepository;
        this.photoService = photoService;
    }

    public Product createProduct(ProductCreateRequest productToCreate) throws Exception {
        Product product = new Product();
        product.setName(productToCreate.getName());
        product.setPrime_cost(productToCreate.getPrime_cost());
        product.setPrice(productToCreate.getPrice());
        product.setSku(productToCreate.getSku());
        product.setDescription(productToCreate.getDescription());
        product.setCategory(productToCreate.getCategory());

        if(!productToCreate.getColor().equals("none")) product.setColor(productToCreate.getColor());
        else product.setColor(null);

        product.setSize(ProductSize.values()[productToCreate.getSize()]);

        try {
            String firstPhoto = photoService.savePhoto(productToCreate.getPhoto());
            product.getPhotos().add(firstPhoto);
        }catch (IOException e){
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }

        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Page<Product> getPagedProducts(int page, int size){
        Pageable pageable = PageRequest.of(page,size);
        return productRepository.findAll(pageable);
    }

    public Product getProductById(Long productId) {
        return productRepository.findById(productId).orElseThrow(() -> new SearchException(SearchException.PRODUCT_NOT_FOUND));
    }


    public List<Product> search(String request) {
        List<Product> products = productRepository.findAllBySku(request);
        products.addAll(productRepository.findAllByName(request));
        return products;
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
}
