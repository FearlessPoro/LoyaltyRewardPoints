package com.example.loyaltyrewardpoints.controller;

import com.example.loyaltyrewardpoints.dto.ApplicationUserDto;
import com.example.loyaltyrewardpoints.service.ApplicationUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class ApplicationUserController {

    private final ApplicationUserService userService;

    @Autowired
    public ApplicationUserController(ApplicationUserService applicationUserService) {
        this.userService = applicationUserService;
    }

    @GetMapping("/{applicationUserId}")
    public ResponseEntity<ApplicationUserDto> getApplicationUserById(@PathVariable final Long applicationUserId) {
        try {
            return ResponseEntity.ok(userService.getUserById(applicationUserId));
        } catch (ChangeSetPersister.NotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping
    public ResponseEntity<ApplicationUserDto> createApplicationUser(@RequestBody final ApplicationUserDto applicationUserDto) {
        return ResponseEntity.ok(userService.createUser(applicationUserDto));
    }
}
