package ru.yandex.explore.user;

import ru.yandex.explore.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto addNewUser(UserDto userDto);

    List<UserDto> getUsersByIds(List<Long> ids);

    UserDto getUserById(Long userId);

    void deleteUser(Long userId);
}
