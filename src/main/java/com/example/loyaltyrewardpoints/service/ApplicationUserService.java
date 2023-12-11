package com.example.loyaltyrewardpoints.service;

import com.example.loyaltyrewardpoints.dto.ApplicationUserDto;
import com.example.loyaltyrewardpoints.mapper.UserMapper;
import com.example.loyaltyrewardpoints.model.ApplicationUser;
import com.example.loyaltyrewardpoints.repostitory.ApplicationUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ApplicationUserService {

    private final ApplicationUserRepository userRepository;

    @Autowired
    public ApplicationUserService(final ApplicationUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ApplicationUserDto createUser(final ApplicationUserDto userDto) {
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new RuntimeException("User with that email " + userDto.getEmail() + " already exists");
        }
        final ApplicationUser user = UserMapper.mapToEntity(userDto);
        user.setEmail(userDto.getEmail());
        user.setRewardPointsForLastThreeMonths(BigDecimal.ZERO);
        return UserMapper.mapToDto(userRepository.save(user));
    }

    public ApplicationUserDto getUserById(final Long userId) throws ChangeSetPersister.NotFoundException {
        return UserMapper.mapToDto(userRepository.findById(userId)
                .orElseThrow(ChangeSetPersister.NotFoundException::new));
    }

    public void addUserRewardPoints(final Long userId, final BigDecimal rewardPoints) throws ChangeSetPersister.NotFoundException {
        final ApplicationUser user = userRepository.findById(userId)
                .orElseThrow(ChangeSetPersister.NotFoundException::new);
        user.setRewardPointsForLastThreeMonths(user.getRewardPointsForLastThreeMonths().add(rewardPoints));
        userRepository.save(user);
    }

    public void removeUserRewardPoints(final Long userId, final BigDecimal rewardPoints) throws ChangeSetPersister.NotFoundException {
        final ApplicationUser user = userRepository.findById(userId)
                .orElseThrow(ChangeSetPersister.NotFoundException::new);
        user.setRewardPointsForLastThreeMonths(user.getRewardPointsForLastThreeMonths().subtract(rewardPoints));
        userRepository.save(user);
    }
}
