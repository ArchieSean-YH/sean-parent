package top.archiesean.oauth.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.cloud.bootstrap.encrypt.KeyProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.ProviderSettings;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;
import org.springframework.security.web.SecurityFilterChain;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

/**
 * 配置类
 *
 * @Author ArchieSean
 * @Date 2022-06-11 21:46
 */
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig {

    @Resource
    private AuthorizationUserDetail userDetails;
    @Resource
    private DataSource dataSource;
    @Resource
    private KeyProperties keyProperties;

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
     * 客户端管理，使用数据库存储客户端数据
     *
     * @return RegisteredClientRepository
     */
    @Bean
    public RegisteredClientRepository registeredClientRepository() {
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
     * 使用自生成的RSA密钥，方便外部系统使用公钥对于jwt进行解析，获取相关数据
     *
     * @return keyPair
     */
    private KeyPair generateRsaKey() {
        //keyProperties的配置来自配置文件，查看源码即可看到配置前缀
        return new KeyStoreKeyFactory(
                //证书路径
                keyProperties.getKeyStore().getLocation(),
                //密钥
                keyProperties.getKeyStore().getSecret().toCharArray())
                .getKeyPair(
                        //证书别名
                        keyProperties.getKeyStore().getAlias(),
                        //证书密码
                        keyProperties.getKeyStore().getPassword().toCharArray());
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
