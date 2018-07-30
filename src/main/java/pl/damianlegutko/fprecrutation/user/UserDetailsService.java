package pl.damianlegutko.fprecrutation.user;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.damianlegutko.fprecrutation.Validators;

import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;

@Service("userDetailsService")
@AllArgsConstructor
class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    private final UserRepository userRepository;

    @SneakyThrows
    @Transactional(readOnly=true)
    public UserDetails loadUserByUsername(String username) {
        Validators.objectIsNotNull(username, "username");

        User user = userRepository.findByUsername(username.toLowerCase());

        Set<GrantedAuthority> grantedAuthorities = newHashSet();
        user.getRoles().forEach(role -> grantedAuthorities.add(new SimpleGrantedAuthority(role.getName())));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), grantedAuthorities);
    }
}
