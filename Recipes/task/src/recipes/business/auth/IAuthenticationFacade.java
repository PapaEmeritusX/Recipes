package recipes.business.auth;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

public interface IAuthenticationFacade {
    Authentication getAuthentication();
}
