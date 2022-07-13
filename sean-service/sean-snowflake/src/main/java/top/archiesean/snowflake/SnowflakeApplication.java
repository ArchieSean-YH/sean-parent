package top.archiesean.snowflake;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 雪花服务
 *
 * @Author ArchieSean
 * @since 2022-07-12 11:01
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class SnowflakeApplication {
    public static void main(String[] args) {
        SpringApplication.run(SnowflakeApplication.class, args);
    }
}
