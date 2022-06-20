package com.blog.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blog.payloads.ApiResponse;
import com.blog.payloads.CategoryDto;
import com.blog.services.CategoryService;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
	
	@Autowired
	private CategoryService categoryService; 
	
	//POST
	@PostMapping("/")
	public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto){
		CategoryDto savedDto=this.categoryService.createCategory(categoryDto);
		
		return new ResponseEntity<CategoryDto>(savedDto,HttpStatus.CREATED);
	}
	
	//Update
	@PutMapping("/{categoryId}")
	public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto,@PathVariable Integer categoryId){
		CategoryDto savedDto=this.categoryService.updateCategory(categoryDto,categoryId);
		
		return new ResponseEntity<CategoryDto>(savedDto,HttpStatus.OK);
	}
	
	//Delete
		@DeleteMapping("/{categoryId}")
		public ResponseEntity<ApiResponse> createCategory(@PathVariable Integer categoryId){
			this.categoryService.deleteCategory(categoryId);
			ApiResponse res=new ApiResponse("Category deleted successfully!!",true);
			return new ResponseEntity<ApiResponse>(res,HttpStatus.OK);
		}
		
		//get
		@GetMapping("/{categoryId}")
		public ResponseEntity<CategoryDto> getCategory(@PathVariable Integer categoryId){
			CategoryDto Dto=this.categoryService.getCategory(categoryId);
			
			return new ResponseEntity<CategoryDto>(Dto,HttpStatus.OK);
		}
		
		@GetMapping("/")
		public ResponseEntity<List<CategoryDto>> getAllCategory(){
			List<CategoryDto> categories=this.categoryService.getAllCategory();
			
			return ResponseEntity.ok(categories);
		}

}
