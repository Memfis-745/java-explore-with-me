package ru.practicum.user;

import lombok.experimental.UtilityClass;
import ru.practicum.user.dto.NewUserDto;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.UserShortDto;
import ru.practicum.user.model.User;

@UtilityClass
public class UserMapper {

    public UserDto userToDto(User user) {
        return new UserDto(
                user.getEmail(),
                user.getId(),
                user.getName()
        );
    }

    public User dtoToUser(NewUserDto userDto) {
        return new User(
                null,
                userDto.getName(),
                userDto.getEmail()
        );
    }

    public UserShortDto userToShortDto(User user) {
        return new UserShortDto(
                user.getId(),
                user.getName()
        );
    }
}