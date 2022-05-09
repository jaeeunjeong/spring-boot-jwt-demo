package me.toyproject.loginjwt.service;

import me.toyproject.loginjwt.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findOneWithAuthoritiesByUsername(username)
                .map(user -> createUser(username, user))
                .orElseThrow(() -> new UsernameNotFoundException(username + ": 데이터베이스에서 찾을 수 없습니다."));
    }

    private User createUser(String username, me.toyproject.loginjwt.entity.User user) {
        if (!user.isActivated()) throw new RuntimeException(username + " 해당 username 은 활성화 되어 있지 않습니다.");
        List<GrantedAuthority> grantedAuthorityList = user.getAuthoritySet().stream().map((authority -> new SimpleGrantedAuthority(authority.getAuthorityName())))
                .collect(Collectors.toList());

        return new User(user.getUsername(), user.getPassword(), grantedAuthorityList);
    }
}
