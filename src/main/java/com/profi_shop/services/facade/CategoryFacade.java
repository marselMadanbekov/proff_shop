package com.profi_shop.services.facade;

import com.profi_shop.dto.CategoryDTO;
import com.profi_shop.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryFacade {
    public CategoryDTO categoryToCategoryDTO(Category category){
        if(category == null)    return null;
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());
        return categoryDTO;
    }

    public Page<CategoryDTO> mapToProductDTOPage(Page<Category> categoryPage) {
        List<CategoryDTO> categoryDTOList = categoryPage.getContent()
                .stream()
                .map(this::categoryToCategoryDTO)
                .collect(Collectors.toList());

        return new PageImpl<>(categoryDTOList, categoryPage.getPageable(), categoryPage.getTotalElements());
    }
}
