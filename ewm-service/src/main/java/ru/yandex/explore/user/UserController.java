package ru.yandex.explore.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.explore.user.dto.NewUserDto;
import ru.yandex.explore.user.dto.UserDto;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@Validated
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto addNewUser(@RequestBody @Valid NewUserDto userDto) {
        log.info("Creating addNewUser {}{}", userDto.getName(), userDto.getEmail());
        return userService.addNewUser(userDto);
    }

    @GetMapping
    public List<UserDto> getUsers(
            @RequestParam(name = "ids", required = false) Set<Long> ids,
            @RequestParam(name = "from", defaultValue = "0") int from,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("Get users with ids = {}, from={}, size={}", ids, from, size);
        return userService.getUsersByIds(ids, from, size);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable(name = "userId") Long userId) {
        log.info("delete user with id = {}", userId);
        userService.deleteUser(userId);
    }

}
