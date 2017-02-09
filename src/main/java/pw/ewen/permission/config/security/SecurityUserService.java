package pw.ewen.permission.config.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pw.ewen.permission.entity.Role;
import pw.ewen.permission.entity.User;
import pw.ewen.permission.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by wenliang on 17-2-9.
 */
public class SecurityUserService implements UserDetailsService {

    private final UserRepository userRepository;

    public SecurityUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findOne(username);
        Role role = user.getRole();
        if(user != null){
            List<GrantedAuthority> authorities = new ArrayList<>();
            if(role != null){
                authorities.add(new SimpleGrantedAuthority(role.getID()));
            }

            return new org.springframework.security.core.userdetails.User(user.getID(), user.getPassword(), authorities);
        }

        throw new UsernameNotFoundException("User '" + username + "' not found");
    }
}
