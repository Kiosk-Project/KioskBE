package com.study.springboot.domain.user.repository;

import com.study.springboot.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Page<User> findByUserIdContains(String text, Pageable pageable);
    Page<User> findByUserNameContains(String text, Pageable pageable);
    Optional<User> findByUserId(String userId);

    Page<User> findUsersByUserDelYn(Boolean delYn, Pageable pageable);



}
