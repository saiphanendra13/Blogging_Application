package com.blog.services;

import com.blog.payloads.CommentDto;
import com.blog.payloads.CommentResponse;

public interface CommentService {
	
	CommentDto createComment(CommentDto commentDto, Integer postId, Integer userId);
	
	boolean deleteComment(Integer userId,Integer commentId);
	
	CommentResponse updateComment(CommentDto commentDto,Integer commentId, Integer userId);
	

}
