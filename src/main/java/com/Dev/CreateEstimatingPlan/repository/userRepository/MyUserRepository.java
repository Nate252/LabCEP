package com.Dev.CreateEstimatingPlan.repository.userRepository;

import com.Dev.CreateEstimatingPlan.entity.user.MyUser;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MyUserRepository extends JpaRepository<MyUser,Long> {
    Optional<MyUser> findById(Long id);
    Optional<MyUser> findByEmail(String email);
}
