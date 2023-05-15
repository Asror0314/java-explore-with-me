package ru.yandex.explore.user;

import ru.yandex.explore.user.dto.NewUserDto;
import ru.yandex.explore.user.dto.UserDto;
import ru.yandex.explore.user.dto.UserShortDto;

public class UserMapper {
    public static User mapNewUserDto2User(NewUserDto userDto) {
        final User user = new User();

        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        return user;
    }

    public static UserDto mapUser2UserDto(User user) {
        final UserDto userDto = new UserDto();

        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        return userDto;
    }

    public static UserShortDto mapUser2UserShortDto(User user) {
        final UserShortDto userShortDto = new UserShortDto();

        userShortDto.setId(user.getId());
        userShortDto.setName(user.getName());
        return userShortDto;
    }
}
