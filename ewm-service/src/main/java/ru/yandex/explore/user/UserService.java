package ru.yandex.explore.user;

import ru.yandex.explore.user.dto.NewUserDto;
import ru.yandex.explore.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto addNewUser(NewUserDto userDto);

    List<UserDto> getUsersByIds(List<Long> ids);

    UserDto getUserById(Long userId);

    void deleteUser(Long userId);
}
