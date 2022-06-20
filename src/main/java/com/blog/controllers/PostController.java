package com.blog.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.hibernate.engine.jdbc.StreamUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.blog.config.AppConstants;
import com.blog.payloads.ApiResponse;
import com.blog.payloads.PostDto;
import com.blog.payloads.PostResponse;
import com.blog.services.FileService;
import com.blog.services.PostService;

@RestController
@RequestMapping("/api")
public class PostController {
	
	@Autowired
	private PostService postService;
	
	@Autowired
	private FileService fileService;
	
	@Value("${project.image}")
	private String path;
	
	
	@PostMapping("/user/{userId}/category/{categoryId}/post")
	public ResponseEntity<PostDto> createPost(
			@RequestBody PostDto postDto,
			@PathVariable Integer userId, 
			@PathVariable Integer categoryId){
		
		PostDto dto=this.postService.createPost(postDto, userId, categoryId);
		
		return new ResponseEntity<PostDto>(dto,HttpStatus.CREATED);
	}
	
	@GetMapping("/user/{userId}/post")
	public ResponseEntity<List<PostDto>> getAllPostsByUserId(@PathVariable("userId") Integer userId)
	{
		List<PostDto> posts=this.postService.getAllPostsByUser(userId);
		return new ResponseEntity<List<PostDto>>(posts,HttpStatus.OK);
	}
	

	@GetMapping("/category/{categoryId}/post")
	public ResponseEntity<List<PostDto>> getAllPostsByCategoryId(@PathVariable("categoryId") Integer categoryId)
	{
		List<PostDto> posts=this.postService.getAllPostsByCategory(categoryId);
		return new ResponseEntity<List<PostDto>>(posts,HttpStatus.OK);
	}
	
	@GetMapping("/post")
	public ResponseEntity<PostResponse> getAllPosts(
			@RequestParam(value="pageNumber",defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(value="pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(value="sortBy", defaultValue= AppConstants.SORT_BY, required=false) String sortBy,
			@RequestParam(value="sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir
			){
		PostResponse resp=this.postService.getAllPosts(pageNumber,pageSize,sortBy, sortDir);
		return new ResponseEntity<PostResponse>(resp,HttpStatus.OK);
	}
	
	@GetMapping("/post/{postId}")
	public ResponseEntity<PostDto> getPostByPostId(@PathVariable Integer postId){
		PostDto dto=this.postService.getPostById(postId);
		return new ResponseEntity<PostDto>(dto,HttpStatus.OK);
	}
	
	@DeleteMapping("/post/{postId}")
	public ResponseEntity<ApiResponse> deletePostById(@PathVariable Integer postId){
		this.postService.deletePost(postId);
		ApiResponse res=new ApiResponse("Post deleted successfully!!", true);
		return new ResponseEntity<ApiResponse>(res,HttpStatus.OK);
		
	}
	
	@PutMapping("/post/{postId}")
	public ResponseEntity<PostDto> updatePost(@RequestBody PostDto postDto,@PathVariable Integer postId){
		PostDto dto=this.postService.updatePost(postDto, postId);
		
		return new ResponseEntity<PostDto>(dto,HttpStatus.OK);
		
	}

	@GetMapping("/post/search/{keywords}")
	public ResponseEntity<List<PostDto>> searchByPostTitle(@PathVariable("keywords") String keywords){
		
		List<PostDto> posts=this.postService.searchPosts(keywords);
		return new ResponseEntity<List<PostDto>>(posts, HttpStatus.OK);
	}
	
	//Image upload
	@PostMapping("/post/image/upload/{postId}")
	public ResponseEntity<PostDto> uploadPostImage(
			@RequestParam("image") MultipartFile image,
			@PathVariable("postId") Integer postId) throws IOException{
		
		PostDto pdto=this.postService.getPostById(postId);
		
		String fileName=this.fileService.uploadImage(path, image);
		
		pdto.setImageName(fileName);
		PostDto updatedPost=this.postService.updatePost(pdto, postId);
		return new ResponseEntity<PostDto>(updatedPost,HttpStatus.OK);
	}
	
	//get image
	@GetMapping(value="/post/image/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
	public void downloadImage(
			@PathVariable("imageName") String imageName,
			HttpServletResponse response
			) throws IOException {
		InputStream resource=this.fileService.getResource(path, imageName);
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		StreamUtils.copy(resource, response.getOutputStream());
		
	}
	
	

}
