package recipes.presentation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
//import recipes.business.user.MyUserDetails;
import org.springframework.web.server.ResponseStatusException;
import recipes.business.user.UserService;
import recipes.business.user.User;

import javax.validation.Valid;

@RestController
public class UserController {
    @Autowired
    UserService userService;


    @PostMapping("/api/register")
    public void registerUser(@Valid @RequestBody User user) {
        if (userService.existsByEmail(user.getEmail()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad Request");

        userService.save(user);
    }
//        return userService.registerNewUser(user);
//    }

}
