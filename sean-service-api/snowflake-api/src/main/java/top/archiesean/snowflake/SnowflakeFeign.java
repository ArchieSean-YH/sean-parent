package top.archiesean.snowflake;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 提供id生成
 *
 * @Author ArchieSean
 * @Date 2022-07-12 20:06
 */
@FeignClient("sean-snowflake")
public interface SnowflakeFeign {
    /**
     * 获取雪花id
     *
     * @return long
     */
    @GetMapping("id")
    Long nextId();
}
