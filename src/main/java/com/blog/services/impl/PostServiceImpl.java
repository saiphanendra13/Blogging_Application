package com.blog.services.impl;



import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.blog.entities.Category;
import com.blog.entities.Post;
import com.blog.entities.User;
import com.blog.exceptions.ResourceNotFoundException;
import com.blog.payloads.CategoryDto;
import com.blog.payloads.PostDto;
import com.blog.payloads.PostResponse;
import com.blog.repositories.CategoryRepo;
import com.blog.repositories.PostRepo;
import com.blog.repositories.UserRepo;
import com.blog.services.PostService;

@Service
public class PostServiceImpl implements PostService {
	
	@Autowired
	private PostRepo postRepo;
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private CategoryRepo categoryRepo;
	
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public PostDto createPost(PostDto dto, Integer userId, Integer categoryId) {
		
		User user=this.userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("User", "User Id", userId));
		
		Category category=this.categoryRepo.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category","Category Id", categoryId));
		
		
		Post post=this.PostDtoToPost(dto);
		post.setImageName("default.png");
		post.setAddedDate(new Date());
		post.setCategory(category);
		post.setUser(user);
		
		Post savedPost=this.postRepo.save(post);
		
		return this.postToPostDto(savedPost);
	}

	@Override
	public PostDto updatePost(PostDto dto, Integer postId) {
		Post post=this.postRepo.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post","Post Id",postId));
		
		post.setTitle(dto.getTitle());
		post.setContent(dto.getContent());
		post.setImageName(dto.getImageName());
		
		Post updatedPost=this.postRepo.save(post);
		return this.postToPostDto(updatedPost);
	}

	@Override
	public void deletePost(Integer PostId) {
		Post p=this.postRepo.findById(PostId).orElseThrow(()->new ResourceNotFoundException("Post","Post Id",PostId));
		this.postRepo.delete(p);
	}

	@Override
	public PostDto getPostById(Integer postId) {
		Post post=this.postRepo.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post","Post Id",postId));
		return this.postToPostDto(post);
	}

	@Override
	public PostResponse getAllPosts(Integer pageNumber, Integer pageSize, String sortBy,String sortDir) {
		
		//Sorting 
		Sort sort=(sortDir.equalsIgnoreCase("asc"))?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
		
		
		//pagenation
		Pageable p=PageRequest.of(pageNumber, pageSize, sort);
		
		Page<Post> page=this.postRepo.findAll(p);
		
		List<Post> posts=page.getContent();
		
		List<PostDto> dtos=posts.stream().map(post->this.postToPostDto(post)).collect(Collectors.toList());
		
		PostResponse resp=new PostResponse();
		resp.setContent(dtos);
		resp.setPageNumber(page.getNumber());
		resp.setPageSize(page.getSize());
		resp.setTotalElements(page.getTotalElements());
		resp.setTotalPages(page.getTotalPages());
		resp.setLastPage(page.isLast());
		
		return resp;
	}

	@Override
	public List<PostDto> getAllPostsByCategory(Integer categoryId) {
		
		Category cat=this.categoryRepo.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category","Category Id",categoryId));
		
		List<Post> posts=this.postRepo.findByCategory(cat);
		
		List<PostDto> postDtos=posts.stream().map(e->this.postToPostDto(e)).collect(Collectors.toList());
		return postDtos;
	}

	@Override
	public List<PostDto> getAllPostsByUser(Integer userId) {
		User user=this.userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("User","User Id",userId));
		
		List<Post> posts=this.postRepo.findByUser(user);
		
		List<PostDto> dtos=posts.stream().map(post->this.postToPostDto(post)).collect(Collectors.toList());
		
		return dtos;
	}

	@Override
	public List<PostDto> searchPosts(String keyword) {
		List<Post> posts=this.postRepo.searchByTitle("%"+keyword+"%");
		
		List<PostDto> dtos=posts.stream().map(post->this.postToPostDto(post)).collect(Collectors.toList());
		return dtos;
	}
	
	public PostDto postToPostDto(Post post) {
		return this.modelMapper.map(post, PostDto.class);
	}
	
	public Post PostDtoToPost(PostDto dto) {
		return this.modelMapper.map(dto, Post.class);
	}

}
