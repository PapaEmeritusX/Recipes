package recipes.business.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import recipes.business.user.User;
import recipes.persistence.UserRepository;

@Service
public class AuthenticationFacade implements IAuthenticationFacade {
    @Override
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Autowired
    private UserRepository userRepository;

    public User getCurrentUser() {
        Authentication authentication = getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            Object loggedUser = authentication.getPrincipal();
            System.out.println("Here NO RESPONSE STATUS UNAUTHORIZED");

            return userRepository.findById(((User) loggedUser).getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        }
        System.out.println("Here RESPONSE STATUS UNAUTHORIZED");
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }
}
