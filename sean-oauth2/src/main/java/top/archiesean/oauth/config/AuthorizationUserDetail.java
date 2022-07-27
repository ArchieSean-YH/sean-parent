package top.archiesean.oauth.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * userDetail Service
 *
 * @author ArchieSean
 * @since 2022-07-26 23:44
 */
@Component
public class AuthorizationUserDetail implements UserDetailsService {

    /**
     * 根据用户名查询用户信息，封装用户信息
     *
     * @param username 用户名
     * @return UserDetails
     * @throws UsernameNotFoundException 异常
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //获取认证信息(当前请求中的认证信息)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        //没有认证信息时，失败

        //有认证信息时，获取RBAC权限列表【此处使用方法级权限控制】

        return null;
    }
}
