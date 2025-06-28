package telran.java57.farmmarket.security;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import telran.java57.farmmarket.dao.UserRepository;
import telran.java57.farmmarket.model.User;

import java.util.Collection;


@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        Collection<String> authorities = user.getRoles().stream()
                .map(r -> "ROLE_" + r.name())
                .toList();
        return new org.springframework.security.core.userdetails.User(username,user.getPassword(), AuthorityUtils.createAuthorityList(authorities));
    }
}
