package ru.yandex.explore.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.explore.exception.NotFoundException;
import ru.yandex.explore.user.dto.UserDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private final UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDto addNewUser(UserDto userDto) {
        final User user = UserMapper.map2User(userDto);
        User addedUser = repository.save(user);
        return UserMapper.map2UserDto(addedUser);
    }

    @Override
    public List<UserDto> getUsersByIds(List<Long> ids) {
        return repository.findAllById(ids)
                .stream()
                .map(UserMapper::map2UserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(Long userId) {
        final User user = repository.findById(userId)
                .orElseThrow(
                        () -> new NotFoundException(String.format("User id = %d not found", userId)));

        return UserMapper.map2UserDto(user);
    }

    @Override
    public void deleteUser(Long userId) {
        getUserById(userId);
        repository.deleteById(userId);
    }

}
