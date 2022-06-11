package com.example.task5.repo;

import com.example.task5.entity.User;
import com.example.task5.projection.UserProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {

    @Query(
            nativeQuery = true,
            value = "select u.id   as id," +
                    "       u.name as name " +
                    "from users u " +
                    "where u.id = :userId"
    )
    UserProjection getUserProjectionById(@Param("userId") Long userId);

    @Query(
            nativeQuery = true,
            value = "select u.id   as id," +
                    "       u.name as name " +
                    "from users u "
    )
    List<UserProjection> getAllUsers();

    Optional<User> findByName(String name);

    @Query(
            nativeQuery = true,
            value = "select u.id   as id, " +
                    "       u.name as name " +
                    "from users u\n" +
                    "where lower(u.name) like concat('%', lower(:searchText), '%')"
    )
    List<UserProjection> searchUsers(@Param("searchText") String searchText);
}
