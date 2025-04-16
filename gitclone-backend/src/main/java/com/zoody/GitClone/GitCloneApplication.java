package com.zoody.GitClone;

import com.zoody.GitClone.service.FetchUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class GitCloneApplication {



	public static void main(String[] args) {
		SpringApplication.run(GitCloneApplication.class, args);

	}
}
