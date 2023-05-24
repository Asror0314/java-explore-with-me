package ru.yandex.explore.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = "SELECT u.* from explore.users as u " +
            "WHERE u.id in :ids or 0 in :ids " +
            "LIMIT :size OFFSET :from", nativeQuery = true)
    List<User> findUsersByIds(
            @Param(value = "ids") Set<Long> ids,
            @Param(value = "from") int from,
            @Param(value = "size") int size
    );
}
