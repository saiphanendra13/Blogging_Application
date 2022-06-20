package com.blog.payloads;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CategoryDto {
	
	
	private int categoryId;
	
	@NotBlank
	@Size(min = 4, max=100,message = "Enter title with min 4 chars and max 100 chars")
	private String categoryTitle;
	
	@NotBlank
	@Size(min=10,message = "Description must contain minimum of 10 characters")
	private String categoryDescription;


}
