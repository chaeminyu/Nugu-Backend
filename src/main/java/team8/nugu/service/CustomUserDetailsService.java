package team8.nugu.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import team8.nugu.dto.CustomUserDetails;
import team8.nugu.entity.Users;
import team8.nugu.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Users userData = userRepository.findByUsername(username);
        if (userData != null) {
            return new CustomUserDetails(userData);
        }

        return null;
    }
}
