package com.example.loyaltyrewardpoints;

import com.example.loyaltyrewardpoints.dto.ApplicationUserDto;
import com.example.loyaltyrewardpoints.model.ApplicationUser;
import com.example.loyaltyrewardpoints.repostitory.ApplicationUserRepository;
import com.example.loyaltyrewardpoints.service.ApplicationUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ApplicationUserServiceTest {

    @Mock
    private ApplicationUserRepository userRepository;

    @InjectMocks
    private ApplicationUserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUser_Success() {
        long expectedId = 1L;
        ApplicationUserDto userDto = new ApplicationUserDto();
        userDto.setEmail("test@example.com");

        when(userRepository.findByEmail(userDto.getEmail())).thenReturn(Optional.empty());

        ApplicationUser savedUser = new ApplicationUser();
        savedUser.setId(expectedId);
        savedUser.setEmail(userDto.getEmail());
        savedUser.setRewardPointsForLastThreeMonths(BigDecimal.ZERO);

        when(userRepository.save(any(ApplicationUser.class))).thenReturn(savedUser);

        ApplicationUserDto result = userService.createUser(userDto);

        assertNotNull(result);
        assertEquals(savedUser.getId(), expectedId);
        assertEquals(savedUser.getEmail(), result.getEmail());


        verify(userRepository, times(1)).findByEmail(userDto.getEmail());
        verify(userRepository, times(1)).save(any(ApplicationUser.class));
    }

    @Test
    void testCreateUser_UserAlreadyExists() {
        ApplicationUserDto userDto = new ApplicationUserDto();
        userDto.setEmail("existing@example.com");

        ApplicationUser existingUser = new ApplicationUser();
        existingUser.setId(1L);
        existingUser.setEmail(userDto.getEmail());
        existingUser.setRewardPointsForLastThreeMonths(BigDecimal.ZERO);

        when(userRepository.findByEmail(userDto.getEmail())).thenReturn(Optional.of(existingUser));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.createUser(userDto));
        assertEquals("User with that email " + userDto.getEmail() + " already exists", exception.getMessage());

        verify(userRepository, times(1)).findByEmail(userDto.getEmail());
        verify(userRepository, never()).save(any(ApplicationUser.class));
    }

}
