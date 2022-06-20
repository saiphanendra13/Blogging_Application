package com.blog;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.blog.config.AppConstants;
import com.blog.entities.Role;
import com.blog.repositories.RoleRepo;

@SpringBootApplication
public class BlogAppApisApplication implements CommandLineRunner {
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private RoleRepo roleRepo;

	public static void main(String[] args) {
		SpringApplication.run(BlogAppApisApplication.class, args);
	}
	
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Override
	public void run(String... args) throws Exception {
		
		//encode
		System.out.println(passwordEncoder.encode("xyz"));
		try {
			
			Role role=new Role();
			role.setId(AppConstants.ROLE_ADMIN);
			role.setRole("ROLE_ADMIN");
			
			Role role1=new Role();
			role1.setId(AppConstants.ROLE_NORMAL);
			role1.setRole("ROLE_NORMAL");
			
			List<Role> roles=List.of(role,role1);
			
			List<Role> savedRoles=this.roleRepo.saveAll(roles);
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	

}
