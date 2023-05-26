package ru.yandex.explore.user;

import ru.yandex.explore.user.dto.NewUserDto;
import ru.yandex.explore.user.dto.UserDto;

import java.util.List;
import java.util.Set;

public interface UserService {
    UserDto addNewUser(NewUserDto userDto);

    List<UserDto> getUsersByIds(Set<Long> ids, int from, int size);

    UserDto getUserById(Long userId);

    void deleteUser(Long userId);
}
