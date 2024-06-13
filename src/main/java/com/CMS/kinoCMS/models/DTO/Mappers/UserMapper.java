package com.CMS.kinoCMS.models.DTO.Mappers;

import com.CMS.kinoCMS.models.DTO.UserResponseDto;
import com.CMS.kinoCMS.models.User;

public class UserMapper {
    public static UserResponseDto toUserResponseDto(User user) {
        UserResponseDto dto = new UserResponseDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setAddress(user.getAddress());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setDateOfBirthday(user.getDateOfBirthday());
        dto.setGender(user.getGender());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setRole(user.getRole());
        dto.setCityId(user.getCity().getId());
        return dto;
    }
}
