package ru.yandex.explore.user;

import ru.yandex.explore.user.dto.UserDto;

public class UserMapper {
    public static User map2User(UserDto userDto) {
        final User user = new User();

        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        return user;
    }

    public static UserDto map2UserDto(User user) {
        final UserDto userDto = new UserDto();

        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        return userDto;
    }
}
