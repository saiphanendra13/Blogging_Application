package com.blog.services;

import java.util.List;

import com.blog.entities.Post;
import com.blog.payloads.PostDto;
import com.blog.payloads.PostResponse;

public interface PostService {
	
	PostDto createPost(PostDto dto, Integer userId, Integer categoryId);
	
	PostDto updatePost(PostDto dto, Integer postId);
	
	void deletePost(Integer PostId);
	
	PostDto getPostById(Integer postId);
	
	PostResponse getAllPosts(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
	
	List<PostDto> getAllPostsByCategory(Integer categoryId);
	
	List<PostDto> getAllPostsByUser(Integer userId);
	
	List<PostDto> searchPosts(String keyword);

	
	
	

}
