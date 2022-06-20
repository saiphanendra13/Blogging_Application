package com.blog.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blog.entities.Category;
import com.blog.exceptions.ResourceNotFoundException;
import com.blog.payloads.CategoryDto;
import com.blog.repositories.CategoryRepo;
import com.blog.services.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {
	
	@Autowired
	private CategoryRepo categoryRepo;
	
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public CategoryDto createCategory(CategoryDto dto) {
		Category cat=this.categoryDtoTocategory(dto);
		
		Category savedCat=this.categoryRepo.save(cat);
		return this.categoryTocategoryDto(savedCat);
	}

	@Override
	public CategoryDto updateCategory(CategoryDto dto, Integer categoryId) {
		
		Category cat=this.categoryRepo.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Categoty", "Category Id", categoryId));
		
		cat.setCategoryTitle(dto.getCategoryTitle());
		cat.setCategoryDescription(dto.getCategoryDescription());
	
		Category savedCat=this.categoryRepo.save(cat);
		return this.categoryTocategoryDto(savedCat);
	}

	@Override
	public void deleteCategory(Integer categoryId) {
		Category cat=this.categoryRepo.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Categoty", "Category Id", categoryId));
		
		this.categoryRepo.delete(cat);

	}

	@Override
	public CategoryDto getCategory(Integer categoryId) {
		Category cat=this.categoryRepo.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Categoty", "Category Id", categoryId));
		
		return this.categoryTocategoryDto(cat);
	}

	@Override
	public List<CategoryDto> getAllCategory() {
		List<Category> cats=this.categoryRepo.findAll();
		
		List<CategoryDto> dtos = new ArrayList<CategoryDto>();
		
		cats.forEach(e->dtos.add(this.categoryTocategoryDto(e)));
		
		return dtos;
	}
	
	public CategoryDto categoryTocategoryDto(Category category) {
		return this.modelMapper.map(category, CategoryDto.class);
	}
	
	public Category categoryDtoTocategory(CategoryDto dto) {
		return this.modelMapper.map(dto, Category.class);
	}


}
