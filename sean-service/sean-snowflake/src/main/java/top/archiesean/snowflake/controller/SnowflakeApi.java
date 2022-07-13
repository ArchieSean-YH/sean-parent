package top.archiesean.snowflake.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.archiesean.snowflake.service.ISnowflake;

/**
 * 雪花id
 *
 * @Author ArchieSean
 * @Date 2022-07-12 19:56
 */
@RestController
@RequestMapping("snowflake")
public class SnowflakeApi {

    private final ISnowflake iSnowflake;

    public SnowflakeApi(ISnowflake iSnowflake) {
        this.iSnowflake = iSnowflake;
    }

    /**
     * 获取雪花id
     *
     * @return long
     */
    @GetMapping("id")
    public Long nextId() {
        return iSnowflake.nextId();
    }
}
