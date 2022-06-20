package com.blog.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.blog.config.AppConstants;
import com.blog.entities.Role;
import com.blog.entities.User;
import com.blog.exceptions.ResourceNotFoundException;
import com.blog.payloads.UserDto;
import com.blog.repositories.RoleRepo;
import com.blog.repositories.UserRepo;
import com.blog.services.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private RoleRepo roleRepo;

	@Override
	public UserDto createUser(UserDto userDto) {
		User u=dtoToUser(userDto);
		
		User savedUser=this.userRepo.save(u);
		return this.userToDto(savedUser);
	}

	@Override
	public UserDto updateUser(UserDto userDto, Integer userId) {
		User u=this.userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("User","id",userId));
		
		u.setName(userDto.getName());
		u.setEmail(userDto.getEmail());
		u.setPassword(userDto.getPassword());
		u.setAbout(userDto.getAbout());
		
		User savesUser=this.userRepo.save(u);
		
		return this.userToDto(savesUser);
	}

	@Override
	public UserDto getUserById(Integer userId) {
		User u=this.userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("User","id",userId));
		
		return this.userToDto(u);
	}

	@Override
	public List<UserDto> getAllUsers() {
		List<User> users=this.userRepo.findAll();
		
		List<UserDto> userDtos=users.stream().map(user->this.userToDto(user)).collect(Collectors.toList());
		return userDtos;
	}

	@Override
	public void deleteUser(Integer userId) {
		
		User u=this.userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("User","id",userId));
		
		this.userRepo.delete(u);
	}
	
	public User dtoToUser(UserDto dto) {
		User u =this.modelMapper.map(dto, User.class);
		
		/*
		User user=new User();
		user.setId(dto.getId());
		user.setName(dto.getName());
		user.setEmail(dto.getEmail());
		user.setPassword(dto.getPassword());
		user.setAbout(dto.getAbout());
		*/
		
		return u;		
	}
	
	public UserDto userToDto(User user) {
		UserDto dto =this.modelMapper.map(user, UserDto.class);
		return dto;
	}

	@Override
	public UserDto registerNewUser(UserDto dto) {
		
		User user=this.dtoToUser(dto);
		
		//password encode
		user.setPassword(this.passwordEncoder.encode(dto.getPassword()));
		
		Role role=this.roleRepo.findById(AppConstants.ROLE_NORMAL).orElseThrow(()-> new ResourceNotFoundException("Role", "Role Id",AppConstants.ROLE_NORMAL));
		
		user.getRoles().add(role);
		
		User savedUser=this.userRepo.save(user);
		
		return this.userToDto(savedUser);
	}

}
