package com.lib.controller;

import com.lib.dto.CategoryDTO;
import com.lib.dto.request.CategoryRequest;
import com.lib.dto.response.LibResponse;
import com.lib.dto.response.ResponseMessage;
import com.lib.service.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/visitors/page")

    public ResponseEntity<Page<CategoryDTO>> getAllCategoryByPage(@RequestParam("page") int page,
                                                                  @RequestParam("size") int size,
                                                                  @RequestParam("sort") String prop,
                                                                  @RequestParam(value = "direction",
                                                                          required=false,
                                                                          defaultValue = "DESC") Sort.Direction direction
    ){

        Pageable pageable= PageRequest.of(page,size,Sort.by(direction,prop));
        Page<CategoryDTO> categoryDTOPage=categoryService.getAllCategoryByPage(pageable);

        return ResponseEntity.ok(categoryDTOPage);

    }
    @GetMapping("/visitors")
    public ResponseEntity<List<CategoryDTO>> getAllCategory(){

        List<CategoryDTO> categoryDTOList=categoryService.getAllCategory();

        return ResponseEntity.ok(categoryDTOList);

    }
    @GetMapping("visitors/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id){
       CategoryDTO categoryDTO= categoryService.getCategoryById(id);

        return ResponseEntity.ok(categoryDTO);

    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LibResponse> creationCategory(@Valid @RequestBody CategoryRequest creationCategoryRequest){
        categoryService.createCategory(creationCategoryRequest);

        LibResponse response=
                new LibResponse(ResponseMessage.CATEGORY_CREATED_RESPONSE_MESSAGE,true);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LibResponse> updateCategory(@PathVariable Long id,
                                                      @Valid @RequestBody CategoryRequest categoryRequest){
        categoryService.updateCategory(id,categoryRequest);

        LibResponse libResponse=new LibResponse(ResponseMessage.CATEGORY_UPDATED_RESPONSE_MESSAGE,true);

        return ResponseEntity.ok(libResponse);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LibResponse> deleteResponse(@PathVariable Long id){
        categoryService.deleteCategory(id);
         LibResponse libResponse=new LibResponse(ResponseMessage.CATEGORY_DELETED_RESPONSE_MESSAGE,true);
         return ResponseEntity.ok(libResponse);
    }



}
