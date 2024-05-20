package com.itteam.estatesapi.mapper;

import com.itteam.estatesapi.model.User;
import com.itteam.estatesapi.rest.dto.UserDto;

public interface UserMapper {

    UserDto toUserDto(User user);
}