package com.profi_shop.services.facade;

import com.profi_shop.dto.ProductDTO;
import com.profi_shop.dto.ProductDetailsDTO;
import com.profi_shop.model.*;
import com.profi_shop.repositories.ProductVariationRepository;
import com.profi_shop.services.ReviewService;
import com.profi_shop.services.StockService;
import com.profi_shop.services.StoreHouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductFacade {
    private final StockService stockService;
    private final ReviewService reviewService;
    private final CategoryFacade categoryFacade;
    private final ProductVariationRepository productVariationRepository;

    private final StoreHouseService storeHouseService;

    @Autowired
    public ProductFacade(StockService stockService, ReviewService reviewService, CategoryFacade categoryFacade, ProductVariationRepository productVariationRepository, StoreHouseService storeHouseService) {
        this.stockService = stockService;
        this.reviewService = reviewService;
        this.categoryFacade = categoryFacade;
        this.productVariationRepository = productVariationRepository;
        this.storeHouseService = storeHouseService;
    }

    public ProductDetailsDTO productToProductDetailsDTO(Product product) {
        List<ProductVariation> productVariations = getVariationByProduct(product);
        ProductDetailsDTO productDTO = new ProductDetailsDTO();
        productDTO.setName(product.getName());
        productDTO.setPhotos(product.getPhotos());
        productDTO.setCategory(categoryFacade.categoryToCategoryDTO(product.getCategory()));
        productDTO.setColor(product.getColor());
        productDTO.setProductVariations(productVariations);
        productDTO.setDescription(product.getDescription());
        productDTO.setId(product.getId());
        productDTO.setAverageRating(reviewService.getAverageReviewByProduct(product));
        productDTO.setDiscount(getDiscountByProduct(product));
        productDTO.setOldPrice(product.getPrice());
        productDTO.setSpecifications(product.getSpecifications());
        productDTO.setStoreHouses(getStoreHousesByProduct(product));
        productDTO.setProductVariationCount(getProductVariationsCount(productVariations));
        productDTO.setNewPrice(getNewPriceByPriceAndDiscount(product.getPrice(), productDTO.getDiscount()));
        productDTO.setAvailable(isAvailable(productVariations));

        return productDTO;
    }

    public ProductDTO productToProductDTO(Product product){
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setPhotos(product.getPhotos());
        productDTO.setPrice(product.getPrice());
        productDTO.setDiscount(getDiscountByProduct(product));
        productDTO.setNewPrice(getNewPriceByPriceAndDiscount(product.getPrice(),productDTO.getDiscount()));
        productDTO.setOldPrice(product.getPrice());
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

    private List<ProductVariation> getVariationByProduct(Product product){
        return productVariationRepository.findByParent(product);
    }

    private List<StoreHouse> getStoreHousesByProduct(Product product){
        List<StoreHouse> storeHouses = new ArrayList<>();
        for(ProductVariation productVariation : getVariationByProduct(product)){
            storeHouses.addAll(storeHouseService.getStoreHousesByProduct(productVariation));
        }
        return storeHouses;
    }

    private Map<ProductVariation, Integer> getProductVariationsCount(List<ProductVariation> productVariations){
        Map<ProductVariation, Integer> response = new HashMap<>();
        for(ProductVariation productVariation : productVariations){
            response.put(productVariation, getCountOfProductVariation(productVariation));
        }
        return response;
    }


    private int getCountOfProductVariation(ProductVariation productVariation){
        List<StoreHouse> storeHouses = storeHouseService.getStoreHousesByProduct(productVariation);
        return storeHouses.stream()
                .mapToInt(StoreHouse :: getQuantity) // Получаем поток числовых значений
                .sum();
    }

    private int getNewPriceByPriceAndDiscount(int price, int discount) {
        if (discount > 0) return price - (int) (price * ((double) discount / 100));
        return 0;
    }

    private boolean isAvailable(List<ProductVariation> productVariations){
        for(ProductVariation var : productVariations){
            if(getCountOfProductVariation(var) > 0) return true;
        }
        return false;
    }
}
