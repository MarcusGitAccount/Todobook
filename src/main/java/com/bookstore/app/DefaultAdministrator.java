package com.bookstore.app;

import com.bookstore.app.controller.util.Constants;
import com.bookstore.app.entity.User;
import com.bookstore.app.repo.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCrypt;

@Configuration
@Slf4j
public class DefaultAdministrator {

  @Bean(name = "defaultAdminUserCreation")
  public CommandLineRunner defaultAdminUserCreation(UserRepo userRepo) {
    String password = "123abc";
    String salt = BCrypt.gensalt(Constants.BCRYPT_SALT_LOG_ROUNDS);
    String hashPassword = BCrypt.hashpw(password, salt);

    User admin =  User.builder()
        .email("admin@email.com")
        .password(hashPassword)
        .saltedHash(salt)
        .isAdmin(true)
        .build();

    return args -> {
      if (userRepo.findByEmail(admin.getEmail()).orElse(null) != null) {
        log.info("Default admin has been already created.");
      } else {
        userRepo.save(admin);
        log.info("Default admin created.");
      }
    };
  }
}
