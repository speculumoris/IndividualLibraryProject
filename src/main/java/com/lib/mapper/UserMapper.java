package com.lib.mapper;

import com.lib.domain.User;
import com.lib.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO userToUserDTO(User user);

    List<UserDTO> userListToUserDtoList(List<User> userList);
}
