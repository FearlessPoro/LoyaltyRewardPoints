package com.example.loyaltyrewardpoints.controller;

import com.example.loyaltyrewardpoints.commons.RewardPointsCalculator;
import com.example.loyaltyrewardpoints.dto.ApplicationUserDto;
import com.example.loyaltyrewardpoints.service.ApplicationUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/user")
public class ApplicationUserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RewardPointsCalculator.class);

    private final ApplicationUserService userService;

    @Autowired
    public ApplicationUserController(ApplicationUserService applicationUserService) {
        this.userService = applicationUserService;
    }

    @GetMapping("/{applicationUserId}")
    public ResponseEntity<ApplicationUserDto> getApplicationUserById(@PathVariable final Long applicationUserId) {
        LOGGER.info("Getting user with id: {}", applicationUserId);
        try {
            return ResponseEntity.ok(userService.getUserById(applicationUserId));
        } catch (ChangeSetPersister.NotFoundException e) {
            LOGGER.error("User with id: {} not found", applicationUserId);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> createApplicationUser(
            @RequestBody final ApplicationUserDto applicationUserDto) {
        try {
            final ApplicationUserDto createdUser =  userService.createUser(applicationUserDto);
            final String uri = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/api/user/{id}")
                    .buildAndExpand(createdUser.getEmail())
                    .toUriString();

            return ResponseEntity.created(URI.create(uri)).body(createdUser);
        } catch (RuntimeException e) {
            LOGGER.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
}
