package top.archiesean.snowflake.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.archiesean.snowflake.service.ISnowflake;
import top.archiesean.snowflake.utils.IdWork;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 雪花服务
 *
 * @author ArchieSean
 * @date 2022-07-12 17:12
 */
@Service
@Slf4j
public class SnowflakeImpl implements ISnowflake {

    private final IdWork idWork;

    public SnowflakeImpl(IdWork idWork) {
        this.idWork = idWork;
    }

    /**
     * 获取雪花id
     *
     * @return long
     */
    @Override
    public Long nextId() {
        return idWork.snowflakeId();
    }
}
