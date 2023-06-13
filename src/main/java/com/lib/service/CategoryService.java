package com.lib.service;

import com.lib.domain.Category;
import com.lib.dto.CategoryDTO;
import com.lib.dto.request.CategoryRequest;
import com.lib.exception.BadRequestException;
import com.lib.exception.ConflictException;
import com.lib.exception.ResourceNotFoundException;
import com.lib.exception.message.ErrorMessage;
import com.lib.mapper.CategoryMapper;
import com.lib.repository.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    private final BookService bookService;

    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper, BookService bookService) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.bookService = bookService;
    }

    public List<CategoryDTO> getAllCategory() {
        List<Category> getAllCategories=categoryRepository.findAll();
        return categoryMapper.mapCategory(getAllCategories);
    }

    public CategoryDTO getCategoryById(Long id) {
       Category category =categoryRepository.findById(id).orElseThrow
               (()->new ResourceNotFoundException(ErrorMessage.CATEGORY_NOT_FOUND_EXCEPTION));
       return categoryMapper.catToCatDTO(category);
    }

    public void createCategory(CategoryRequest categoryRequest) {

        if(categoryRepository.existsBySequence(categoryRequest.getSequence())){
            throw  new ConflictException(
                   ErrorMessage.SEQUENCE_CONFLICT_MESSAGE);
        }
        Category category=new Category();
        category.setName(categoryRequest.getName());
        category.setBuiltIn(categoryRequest.getBuiltIn());
        category.setSequence(categoryRequest.getSequence());

        categoryRepository.save(category);
    }

    public void updateCategory(Long id, CategoryRequest categoryRequest) {
        Category category=findCategoryById(id);

        if (category.isBuiltIn()){
            throw new BadRequestException(ErrorMessage.NOT_PERMITTED_METHOD_MESSAGE);
        }

        Boolean existSequence=categoryRepository.existsBySequence(categoryRequest.getSequence());

        if (existSequence && categoryRequest.getSequence()!=category.getSequence()){
            throw  new ConflictException(
                    ErrorMessage.SEQUENCE_CONFLICT_MESSAGE);
        }
        category.setName(categoryRequest.getName());
        category.setBuiltIn(categoryRequest.getBuiltIn());
        category.setSequence(categoryRequest.getSequence());

        categoryRepository.save(category);


    }

    private Category findCategoryById(Long id) {
       return categoryRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(ErrorMessage.CATEGORY_NOT_FOUND_EXCEPTION));
    }

    public void deleteCategory(Long id) {
        Category category=findCategoryById(id);

        if (category.isBuiltIn()){
            throw new BadRequestException(ErrorMessage.NOT_PERMITTED_METHOD_MESSAGE);
        }
        categoryRepository.delete(category);
    }

    public Page<CategoryDTO> getAllCategoryByPage(Pageable pageable) {
        Page<Category> categories=categoryRepository.findAll(pageable);

        return categories.map(cat-> categoryMapper.catToCatDTO(cat));
    }
}
