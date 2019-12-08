package com.bookstore.app.controller;

import com.bookstore.app.entity.Company;
import com.bookstore.app.repo.CompanyRepo;
import com.bookstore.app.responsefactory.APIResponseFactory;
import com.bookstore.app.validation.CompanyValidation;
import com.bookstore.app.validation.util.ValidationMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;
import static com.bookstore.app.controller.util.Constants.*;

@RestController
public class CompanyController {

  @Autowired
  private CompanyRepo companyRepo;

  @Autowired
  private CompanyValidation companyValidation;

  @GetMapping("/api/company/test")
  public ResponseEntity getTest() {
    Company company = Company.builder()
        .name("BoringCo")
        .email("boring@email.com")
        .phoneNumber("1089")
        .build();

    if (company == null) {
      return APIResponseFactory.buildDefaultErrorMessage(null, ENTITY_FOUND);
    }
    return APIResponseFactory.buildDefaultSuccesMessage(company, ENTITY_CREATED);
  }

  @GetMapping("/api/company")
  public ResponseEntity getAll() {
    List<Company> result = companyRepo.findAll();

    if (result.isEmpty()) {
      return APIResponseFactory.buildErrorMessage(null, ENTITY_NOT_FOUND, HttpStatus.NOT_FOUND);
    }
    return APIResponseFactory.buildDefaultSuccesMessage(result, ENTITY_FOUND);
  }

  @GetMapping("/admin/company/{id}")
  public ResponseEntity getById(@PathVariable UUID id) {
    Company company = companyRepo.findById(id).orElse(null);

    if (company == null) {
      return APIResponseFactory.buildErrorMessage(null, ENTITY_NOT_FOUND, HttpStatus.NOT_FOUND);
    }
    return APIResponseFactory.buildDefaultSuccesMessage(company, ENTITY_FOUND);
  }

  @PostMapping("/admin/company")
  public ResponseEntity post(@RequestBody Company company) {
    Company result = companyRepo.save(company);

    if (result == null) {
      return APIResponseFactory.buildErrorMessage(null, ENTITY_CREATION_FAILED, HttpStatus.NOT_FOUND);
    }
    return APIResponseFactory.buildSuccesMessage(result, ENTITY_CREATED, HttpStatus.CREATED);
  }

  @PutMapping("/admin/company/{id}")
  public ResponseEntity put(@RequestBody Company company, @PathVariable UUID id) {
    Company existing = companyRepo.findById(id).orElse(null);

    if (existing == null) {
      return APIResponseFactory.buildErrorMessage(null, ENTITY_NOT_FOUND, HttpStatus.NOT_FOUND);
    }

    company.setUid(existing.getUid());

    ValidationMessage validationMessage = companyValidation.validate(company);
    if (!validationMessage.getIsValid()) {
      return APIResponseFactory.buildMessageFromEntityValidation(validationMessage);
    }

    existing = companyRepo.save(company);
    if (existing == null) {
      return APIResponseFactory.buildDefaultErrorMessage(null, ENTITY_ALTERATION_FAILED);
    }

    return APIResponseFactory.buildSuccesMessage(existing, ENTITY_MODIFIED, HttpStatus.OK);
  }

  @DeleteMapping("/admin/company/{id}")
  public ResponseEntity deleteById(@PathVariable UUID id) {
    Boolean wasRemoved = false;
    Company existing = companyRepo.findById(id).orElse(null);

    if (id == null) {
      return APIResponseFactory.buildDefaultErrorMessage(null, ENTITY_NOT_FOUND);
    }
    if (existing == null) {
      return APIResponseFactory.buildErrorMessage(null, ENTITY_DELETE_FAILED, HttpStatus.NOT_FOUND);
    }
    companyRepo.delete(existing);
    return APIResponseFactory.buildDefaultSuccesMessage(existing, ENTITY_DELETED);
  }
}
