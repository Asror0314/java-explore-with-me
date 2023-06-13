package ru.yandex.explore.user;

import org.springframework.stereotype.Service;
import ru.yandex.explore.exception.NotFoundException;
import ru.yandex.explore.user.dto.NewUserDto;
import ru.yandex.explore.user.dto.UserDto;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDto addNewUser(NewUserDto userDto) {
        final User user = UserMapper.mapNewUserDto2User(userDto);
        User addedUser = repository.save(user);
        return UserMapper.mapUser2UserDto(addedUser);
    }

    @Override
    public List<UserDto> getUsersByIds(Set<Long> ids, int from, int size) {
        ids = validIdsIsNull(ids);
        return repository.findUsersByIds(ids, from, size)
                .stream()
                .map(UserMapper::mapUser2UserDto)
                .collect(Collectors.toList());
    }

    private Set<Long> validIdsIsNull(Set<Long> ids) {
        if (ids == null) {
            ids = new HashSet<>();
            ids.add(0L);
        }
        return ids;
    }

    @Override
    public UserDto getUserById(Long userId) {
        final User user = findUserById(userId);

        return UserMapper.mapUser2UserDto(user);
    }

    @Override
    public void deleteUser(Long userId) {
        getUserById(userId);
        repository.deleteById(userId);
    }

    @Override
    public User findUserById(Long userId) {
        return repository.findById(userId)
                .orElseThrow(
                        () -> new NotFoundException(String.format("User with id = %d was not found", userId)));
    }
}
