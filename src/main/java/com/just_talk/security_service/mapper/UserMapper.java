package com.just_talk.security_service.mapper;

import com.just_talk.security_service.dto.UserDto;
import com.just_talk.security_service.entity.UserEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @InheritInverseConfiguration
    UserEntity map (UserDto dto);
    UserDto map (UserEntity userEntity);
}
