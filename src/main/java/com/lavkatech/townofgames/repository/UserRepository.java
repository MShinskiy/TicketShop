package com.lavkatech.townofgames.repository;

import com.lavkatech.townofgames.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findUserByDtprf(String dtprf);
}
