package com.bookstore.app.controller;

import com.bookstore.app.controller.util.Constants;
import com.bookstore.app.entity.AuthenticationJWTToken;
import com.bookstore.app.entity.User;
import com.bookstore.app.middleware.util.MiddlewareUtils;
import com.bookstore.app.repo.AuthenticationJWTTokenRepo;
import com.bookstore.app.repo.UserRepo;
import com.bookstore.app.responsefactory.APIResponseFactory;
import com.bookstore.app.validation.UserValidation;
import com.bookstore.app.validation.util.ValidationMessage;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.Calendar;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

  @Autowired
  private UserRepo userRepo;

  @Autowired
  private AuthenticationJWTTokenRepo authenticationJWTTokenRepo;

  @Autowired
  private UserValidation userValidation;

  @PostMapping("/signup")
  public ResponseEntity signup(@RequestBody User user) {
    ValidationMessage validationMessage = userValidation.validateSignup(user);

    if (!validationMessage.getIsValid()) {
      return APIResponseFactory.buildMessageFromEntityValidation(validationMessage);
    }

    String salt = BCrypt.gensalt(Constants.BCRYPT_SALT_LOG_ROUNDS);
    String hashPassword = BCrypt.hashpw(user.getPassword(), salt);

    user.setPassword(hashPassword);
    user.setSaltedHash(salt);
    user.setIsAdmin(false);

    User created = userRepo.save(user);
    if (created == null) {
      return APIResponseFactory.buildDefaultErrorMessage(validationMessage, Constants.ENTITY_CREATION_FAILED);
    }

    user.setPassword(null);
    user.setSaltedHash(null);
    return APIResponseFactory.buildDefaultSuccesMessage(user, Constants.ENTITY_CREATED);
  }

  @PostMapping("/login")
  public ResponseEntity login(@RequestBody User user) {
    // Sent data might be uncompleted
    User existing = userRepo.findByEmail(user.getEmail()).orElse(null);
    String password = user.getPassword();

    if (existing == null) {
      return APIResponseFactory.buildErrorMessage(null, Constants.ENTITY_NOT_FOUND, HttpStatus.NOT_FOUND);
    }
    if (!BCrypt.hashpw(password, existing.getSaltedHash()).equals(existing.getPassword())) {
      return APIResponseFactory.buildDefaultErrorMessage(null, Constants.INVALID_PASSWORD);
    }

    AuthenticationJWTToken prevToken =
        authenticationJWTTokenRepo.findByUserEmail(user.getEmail()).orElse(null);
    if (prevToken != null) {
      try {
        // trying to parse it. if parsing fails <=> token expired
        String email = MiddlewareUtils.getEmailFromToken(prevToken.getToken());
        return APIResponseFactory.buildDefaultSuccesMessage(null, "Already logged in");
      } catch (Exception ex)  {
        // invalid token in db
        authenticationJWTTokenRepo.delete(prevToken);
      }
    }

    Calendar calendar = Calendar.getInstance();
    // 7 days from now on
    calendar.add(Calendar.DATE, 7);
    Date expirationDate =  calendar.getTime();

    String jwtStr = Jwts.builder()
        .setSubject("auth")
        .claim("email", user.getEmail())
        .setExpiration(expirationDate)
        .signWith(
            SignatureAlgorithm.HS256,
            TextCodec.BASE64.decode(Constants.KEY)
        )
        .compact();

    AuthenticationJWTToken token = AuthenticationJWTToken
        .builder()
        .token(jwtStr).userEmail(user.getEmail())
        .build();

    authenticationJWTTokenRepo.save(token);
    return APIResponseFactory.buildDefaultSuccesMessage(token, Constants.USER_LOGGED_IN);
  }

  @PostMapping("/logout")
  public ResponseEntity logout(@RequestHeader("authorization") String token) {
    if (token == null) {
      return APIResponseFactory.buildDefaultErrorMessage(null, "Token is empty");
    }
    AuthenticationJWTToken jwtToken = authenticationJWTTokenRepo.findById(token).orElse(null);
    if (jwtToken == null) {
      return APIResponseFactory.buildDefaultErrorMessage(null, "Token is invalid");
    }

    authenticationJWTTokenRepo.delete(jwtToken);
    jwtToken.setToken(null);
    return APIResponseFactory.buildDefaultSuccesMessage(jwtToken, "User logged out");
  }
}
