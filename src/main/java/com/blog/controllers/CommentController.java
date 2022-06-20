package com.blog.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blog.payloads.ApiResponse;
import com.blog.payloads.CommentDto;
import com.blog.payloads.CommentResponse;
import com.blog.services.CommentService;

@RestController
@RequestMapping("/api")
public class CommentController {
	
	@Autowired
	private CommentService commentService;
	
	@PostMapping("/user/{userId}/post/{postId}/comment")
	public ResponseEntity<CommentDto> addComment(
			@RequestBody CommentDto commentDto,
			@PathVariable("userId") Integer userId,
			@PathVariable("postId") Integer postId
			){
		
		CommentDto dto=this.commentService.createComment(commentDto, postId, userId);
		return new ResponseEntity<CommentDto>(dto,HttpStatus.OK);
		
	}
	
	@DeleteMapping("/user/{userId}/comment/{commentId}")
	public ResponseEntity<ApiResponse> deleteComment(
			@PathVariable("userId") Integer userId,
			@PathVariable("commentId") Integer commentId){
		
		ApiResponse res=new ApiResponse("user not allowed to Delete comment!!", false);
		boolean deleted=this.commentService.deleteComment(userId,commentId);
		if(deleted) {
			res.setMessage("Comment Successfully Deleted!!");
			res.setSuccess(deleted);
		}
		
		return ResponseEntity.ok(res);
	}
	
	//update comment
	@PutMapping("/user/{userId}/comment/{commentId}")
	public ResponseEntity<CommentResponse> updateComment(@RequestBody CommentDto dto,
			@PathVariable("userId") Integer userId,
			@PathVariable("commentId") Integer commentId){
		CommentResponse resp=this.commentService.updateComment(dto, commentId, userId);
		
		return new ResponseEntity<CommentResponse>(resp, HttpStatus.OK);
		
	}
}
