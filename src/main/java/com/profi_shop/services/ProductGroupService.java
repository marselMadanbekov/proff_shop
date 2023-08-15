package com.profi_shop.services;

import com.profi_shop.exceptions.SearchException;
import com.profi_shop.model.Product;
import com.profi_shop.model.ProductGroup;
import com.profi_shop.model.requests.ProductGroupRequest;
import com.profi_shop.repositories.ProductGroupRepository;
import com.profi_shop.repositories.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ProductGroupService {
    private final ProductGroupRepository productGroupRepository;
    private final ProductRepository productRepository;

    public ProductGroupService(ProductGroupRepository productGroupRepository, ProductRepository productRepository) {
        this.productGroupRepository = productGroupRepository;
        this.productRepository = productRepository;
    }

    public void createProductGroup(ProductGroupRequest productGroupRequest){
        ProductGroup productGroup = new ProductGroup();
        productGroup.setName(productGroupRequest.getName());
        for(Long productId : productGroupRequest.getMembers().keySet()){
            Product product = getProductById(productId);
            productGroup.addProduct(product);
        }
        productGroupRepository.save(productGroup);
    }

    public Page<ProductGroup> productGroupsFiltered(int page, String name, int sort){
        Pageable pageable;
        if(sort == 0)   pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.ASC,"name"));
        else pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC,"name"));

        return productGroupRepository.findAllByFilters(name,pageable);
    }

    private Product getProductById(Long productId){
        return productRepository.findById(productId).orElseThrow(() -> new SearchException(SearchException.PRODUCT_NOT_FOUND));
    }

    public void deleteGroupById(Long groupId) {
        productGroupRepository.delete(getProductGroupById(groupId));
    }

    public ProductGroup getProductGroupById(Long groupId) {
        return productGroupRepository.findById(groupId).orElseThrow(() -> new SearchException(SearchException.PRODUCT_GROUP_NOT_FOUND));
    }

    public void removeProductFromGroup(Long productId, Long groupId) {
        ProductGroup productGroup = getProductGroupById(groupId);
        Product product = getProductById(productId);
        productGroup.removeProduct(product);
        productGroupRepository.save(productGroup);
    }

    public void addProductToGroup(Long productId, Long groupId) {
        ProductGroup productGroup = getProductGroupById(groupId);
        Product product = getProductById(productId);
        productGroup.addProduct(product);
        productGroupRepository.save(productGroup);
    }
}
