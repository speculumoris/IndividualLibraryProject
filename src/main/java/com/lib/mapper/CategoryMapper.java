package com.lib.mapper;

import com.lib.domain.Category;
import com.lib.dto.CategoryDTO;
import com.lib.dto.request.CategoryRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryDTO catToCatDTO(Category category);


    List<CategoryDTO> mapCategory(List<Category> categoryList);



}
