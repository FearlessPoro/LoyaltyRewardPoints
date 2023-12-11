package com.example.loyaltyrewardpoints.controller;

import com.example.loyaltyrewardpoints.commons.RewardPointsCalculator;
import com.example.loyaltyrewardpoints.dto.ApplicationUserDto;
import com.example.loyaltyrewardpoints.service.ApplicationUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
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
            throw new RuntimeException(e);
        }
    }

    @PostMapping
    public ResponseEntity<ApplicationUserDto> createApplicationUser(
            @RequestBody final ApplicationUserDto applicationUserDto) {
        return ResponseEntity.ok(userService.createUser(applicationUserDto));
    }
}
