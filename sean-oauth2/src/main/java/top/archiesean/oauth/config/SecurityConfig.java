package top.archiesean.oauth.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.ClientSettings;
import org.springframework.security.oauth2.server.authorization.config.ProviderSettings;
import org.springframework.security.web.SecurityFilterChain;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

/**
 * 配置类
 *
 * @Author ArchieSean
 * @Date 2022-06-11 21:46
 */
@EnableGlobalMethodSecurity(securedEnabled = true,prePostEnabled = true)
public class SecurityConfig {

    @Resource
    private AuthorizationUserDetail userDetails;
    @Resource
    private DataSource dataSource;

    /**
     * 加密方式，使用Bcrypt散列加密
     *
     * @return BCryptPasswordEncoder
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JdbcTemplate getJdbcTemplate() {
        return new JdbcTemplate(dataSource);
    }

    /**
     * 这个也是个Spring Security的过滤器链，用于Spring Security的身份认证。
     * 同 security的配置
     *
     * @param http http
     * @return 过滤器链
     * @throws Exception 异常
     */
    @Bean
    @Order(1)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http)
            throws Exception {
        http
                .csrf().disable()
                .httpBasic()
                .and()
                .formLogin()
                .and()
                .antMatcher("/login")
                .antMatcher("/layout")
                .authorizeHttpRequests()
                .anyRequest()
                .authenticated()
                .and()
                .userDetailsService(userDetails)
                .passwordManagement(a -> bCryptPasswordEncoder())
        ;
        return http.build();
    }

    /**
     * oauth2 用于第三方认证，RegisteredClientRepository 主要用于管理第三方（每个第三方就是一个客户端）
     *
     * @return RegisteredClientRepository 用于客户端管理
     */
    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
                //客户端id
                .clientId("messaging-client")
                //客户端密钥
                .clientSecret("{noop}secret")
                //客户端认证方式：basic
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                //认证授权方式：授权码
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                //认证授权方式： 刷新token
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                //认证授权方式： 客户端凭证
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                //重定向地址
                .redirectUri("http://127.0.0.1:8080/login/oauth2/code/messaging-client-oidc")
                .redirectUri("http://127.0.0.1:8080/authorized")
                .scope(OidcScopes.OPENID)
                .scope("message.read")
                .scope("message.write")
                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
                .build();
        return new JdbcRegisteredClientRepository(getJdbcTemplate());
    }

    /**
     * 用于给access_token签名使用。
     *
     * @return JWKSource<SecurityContext>
     */
    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        KeyPair keyPair = generateRsaKey();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }

    /**
     * 生成秘钥对，为jwkSource提供服务。
     *
     * @return keyPair
     */
    private static KeyPair generateRsaKey() {
        KeyPair keyPair;
        try {
            //密钥生成器
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
        PrivateKey aPrivate = keyPair.getPrivate();
        PublicKey aPublic = keyPair.getPublic();
        return keyPair;
    }

    /**
     * 配置Authorization Server实例
     *
     * @return ProviderSettings
     */
    @Bean
    public ProviderSettings providerSettings() {
        return ProviderSettings.builder().build();
    }
}
