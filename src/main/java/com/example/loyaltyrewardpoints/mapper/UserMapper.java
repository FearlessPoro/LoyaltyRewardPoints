package com.example.loyaltyrewardpoints.mapper;

import com.example.loyaltyrewardpoints.dto.ApplicationUserDto;
import com.example.loyaltyrewardpoints.model.ApplicationUser;

public class UserMapper {

    public static ApplicationUser mapToEntity(final ApplicationUserDto userDto) {
        ApplicationUser user = new ApplicationUser();
        user.setEmail(userDto.getEmail());
        return user;
    }

    public static ApplicationUserDto mapToDto(final ApplicationUser user) {
        ApplicationUserDto userDto = new ApplicationUserDto();
        userDto.setEmail(user.getEmail());
        return userDto;
    }
}
