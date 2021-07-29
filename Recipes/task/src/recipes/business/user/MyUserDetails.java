//package recipes.business.user;
//
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.util.*;
//import java.util.stream.Collectors;
//
//public class MyUserDetails implements UserDetails {
//
//    private String userEmail;
//    private String password;
//    private List<GrantedAuthority> authorities;
//    private User user;
//
//    public MyUserDetails(User user) {
//        this.userEmail = user.getUsername();
//        this.password = user.getPassword();
////        this.authorities = Arrays.stream(user.getRoles().split(","))
////                                    .map(SimpleGrantedAuthority::new)
////                                    .collect(Collectors.toCollection(ArrayList::new));
//    }
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return this.authorities;
//    }
//
//    @Override
//    public String getPassword() {
//        return this.password;
//    }
//
//    @Override
//    public String getUsername() {
//        return userEmail;
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
//}
