package com.Dev.CreateEstimatingPlan.config.security;

import com.Dev.CreateEstimatingPlan.entity.user.MyUser;
import com.Dev.CreateEstimatingPlan.repository.userRepository.MyUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private MyUserRepository repository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<MyUser> user = repository.findByEmail(email);
        return user.map(MyUserDetails::new).orElseThrow(() -> new UsernameNotFoundException("not found user"));

    }
}
