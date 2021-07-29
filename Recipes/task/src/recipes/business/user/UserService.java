package recipes.business.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import recipes.business.recipe.Recipe;
import recipes.persistence.UserRepository;

import javax.validation.constraints.Email;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public User findByEmail(@Email String email) {
        return userRepository.findByEmail(email);
    }

    public boolean existsByEmail(@Email String email) {
        return userRepository.existsByEmail(email);
    }


    public User registerNewUser(User newUser)  {
        if (userRepository.existsByEmail(newUser.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        System.out.println(userRepository.existsByEmail(newUser.getEmail()));
        System.out.println(newUser.getPassword());
        System.out.println(newUser.getEmail());

        String encryptedPWD = new BCryptPasswordEncoder().encode(newUser.getPassword());
        newUser.setPassword(encryptedPWD);
        return userRepository.save(newUser);
    }

    public User save(User user) {
        String newPassword = new BCryptPasswordEncoder().encode(user.getPassword());
        user.setPassword(newPassword);
        return userRepository.save(user);
    }


}
