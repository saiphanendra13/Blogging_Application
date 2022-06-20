package com.blog.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blog.entities.Comment;
import com.blog.entities.Post;
import com.blog.entities.User;
import com.blog.exceptions.ResourceNotFoundException;
import com.blog.payloads.CommentDto;
import com.blog.payloads.CommentResponse;
import com.blog.repositories.CommentRepo;
import com.blog.repositories.PostRepo;
import com.blog.repositories.UserRepo;
import com.blog.services.CommentService;

@Service
public class CommentServiceImpl implements CommentService {
	
	@Autowired
	private CommentRepo commentRepo;
	
	@Autowired
	private PostRepo postRepo;
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public CommentDto createComment(CommentDto commentDto, Integer postId, Integer userId) {
		
		Post post=this.postRepo.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post", "Post Id", postId));
		User user=this.userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("User", "User Id", userId));
		
		Comment com=this.commentDtoToComment(commentDto);
		com.setPost(post);
		com.setUser(user);
		
		Comment savedComment=this.commentRepo.save(com);
		
		return this.commentToCommentDto(savedComment);
	}

	@Override
	public boolean deleteComment(Integer userId,Integer commentId) {
		
		boolean deleted=false;
		
		Comment comment=this.commentRepo.findById(commentId).orElseThrow(()->new ResourceNotFoundException("Comment", "Comment Id", commentId));
		User user=this.userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("User", "User Id", userId));
		
		if(comment.getPost().getUser().getId()==user.getId() || comment.getUser().getId()==user.getId()) {
			this.commentRepo.delete(comment);
			deleted=true;
		}
		return deleted;
		
	}
	
	@Override
	public CommentResponse updateComment(CommentDto commentDto, Integer commentId, Integer userId) {
		Comment comment=this.commentRepo.findById(commentId).orElseThrow(()->new ResourceNotFoundException("Comment", "Comment Id", commentId));
		User user=this.userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("User", "User Id", userId));
		CommentResponse res=new CommentResponse();
		
		if(comment.getUser().getId()==user.getId()) {
			comment.setContent(commentDto.getContent());
			Comment updatedCom=this.commentRepo.save(comment);
			
			res.setComment(this.commentToCommentDto(updatedCom));
			res.setMessage("Comment updated successfully!!");
			
		}
		else {
			res.setMessage("user cannot update comment!!");
		}
		
		return res;
	}


	
	
	private Comment commentDtoToComment(CommentDto dto) {
		return this.modelMapper.map(dto, Comment.class);
	}
	
	private CommentDto commentToCommentDto(Comment comment) {
		return this.modelMapper.map(comment, CommentDto.class);
	}

}
