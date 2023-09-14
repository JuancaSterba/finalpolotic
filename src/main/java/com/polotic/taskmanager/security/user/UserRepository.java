package com.polotic.taskmanager.security.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);

    @Modifying
    @Query(value = "UPDATE UserEntity u set u.name =:name," +
            " u.surname =:surname," + "u.email =:email where u.id =:id")
    void update(String name, String surname, String email, Long id);
}
