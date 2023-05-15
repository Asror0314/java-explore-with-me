package ru.yandex.explore.user;

import org.springframework.stereotype.Service;
import ru.yandex.explore.exception.NotFoundException;
import ru.yandex.explore.user.dto.NewUserDto;
import ru.yandex.explore.user.dto.UserDto;

import java.util.List;
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
    public List<UserDto> getUsersByIds(List<Long> ids) {
        return repository.findAllById(ids)
                .stream()
                .map(UserMapper::mapUser2UserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(Long userId) {
        final User user = repository.findById(userId)
                .orElseThrow(
                        () -> new NotFoundException(String.format("User id = %d not found", userId)));

        return UserMapper.mapUser2UserDto(user);
    }

    @Override
    public void deleteUser(Long userId) {
        getUserById(userId);
        repository.deleteById(userId);
    }

}
