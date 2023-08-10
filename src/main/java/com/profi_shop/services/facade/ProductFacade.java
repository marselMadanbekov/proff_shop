package com.profi_shop.services.facade;

import com.profi_shop.dto.ProductDTO;
import com.profi_shop.dto.ProductDetailsDTO;
import com.profi_shop.model.Product;
import com.profi_shop.model.Stock;
import com.profi_shop.services.ReviewService;
import com.profi_shop.services.StockService;
import com.profi_shop.services.StoreHouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductFacade {
    private final StockService stockService;
    private final ReviewService reviewService;
    private final CategoryFacade categoryFacade;

    private final StoreHouseService storeHouseService;

    @Autowired
    public ProductFacade(StockService stockService, ReviewService reviewService, CategoryFacade categoryFacade, StoreHouseService storeHouseService) {
        this.stockService = stockService;
        this.reviewService = reviewService;
        this.categoryFacade = categoryFacade;
        this.storeHouseService = storeHouseService;
    }

    public ProductDetailsDTO productToProductDetailsDTO(Product product) {
        ProductDetailsDTO productDTO = new ProductDetailsDTO();
        productDTO.setName(product.getName());
        productDTO.setSku(product.getSku());
        productDTO.setPhotos(product.getPhotos());
        productDTO.setCategory(categoryFacade.categoryToCategoryDTO(product.getCategory()));
        productDTO.setColor(product.getColor());
        productDTO.setSize(product.getSize());
        productDTO.setDescription(product.getDescription());
        productDTO.setId(product.getId());
        productDTO.setAverageRating(reviewService.getAverageReviewByProduct(product));
        productDTO.setDiscount(getDiscountByProduct(product));
        productDTO.setOldPrice(product.getPrice());
        productDTO.setStoreHouses(storeHouseService.getStoreHousesByProduct(product));
        productDTO.setNewPrice(getNewPriceByPriceAndDiscount(product.getPrice(), productDTO.getDiscount()));
        return productDTO;
    }

    public ProductDTO productToProductDTO(Product product){
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setPhotos(product.getPhotos());
        productDTO.setPrice(product.getPrice());
        productDTO.setDiscount(getDiscountByProduct(product));
        return productDTO;
    }

    public Page<ProductDetailsDTO> mapToProductDetailsDTOPage(Page<Product> productPage) {
        List<ProductDetailsDTO> productDTOList = productPage.getContent()
                .stream()
                .map(this::productToProductDetailsDTO)
                .collect(Collectors.toList());

        return new PageImpl<>(productDTOList, productPage.getPageable(), productPage.getTotalElements());
    }
    public Page<ProductDTO> mapToProductDTOPage(Page<Product> productPage) {
        List<ProductDTO> productDTOList = productPage.getContent()
                .stream()
                .map(this::productToProductDTO)
                .collect(Collectors.toList());

        return new PageImpl<>(productDTOList, productPage.getPageable(), productPage.getTotalElements());
    }
    public List<ProductDetailsDTO> mapToProductDetailsDTOList(List<Product> productPage) {
        return productPage.stream()
                .map(this::productToProductDetailsDTO)
                .collect(Collectors.toList());
    }

    private int getDiscountByProduct(Product product) {
        Stock stock = stockService.getStockByProduct(product);
        if (stock != null) return stock.getDiscount();
        else return 0;
    }

    private int getNewPriceByPriceAndDiscount(int price, int discount) {
        if (discount > 0) return price - (int) (price * ((double) discount / 100));
        return 0;
    }
}
