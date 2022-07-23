package top.archiesean.snowflake.utils;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 雪花生成工具类
 *
 * @Author ArchieSean
 * @since 2022-07-12 11:06
 */
@Slf4j
@Component
public class IdWork {
    /**
     * 工作进程位
     */
    private long workerId = 0;
    /**
     * 数据位
     */
    private final long datacenterId = 1;

    private final Snowflake snowflake = IdUtil.getSnowflake(workerId, datacenterId);

    @PostConstruct
    public void init() {
        try {
            workerId = NetUtil.ipv4ToLong(NetUtil.getLocalhostStr());
            log.info("当前机器的workerId：{}", workerId);
        } catch (Exception e) {
            e.printStackTrace();
            log.warn("当前机器的workerId获取失败", e);
            workerId = NetUtil.getLocalhostStr().hashCode();
        }
    }

    /**
     * 获取雪花id
     * @return long
     */
    public synchronized long snowflakeId() {
        return snowflake.nextId();
    }

    public synchronized long snowflakeId(long workerId, long datacenterId) {
        return IdUtil.getSnowflake(workerId, datacenterId).nextId();
    }

    public static void main(String[] args) {
        IdWork idWork = new IdWork();
        System.out.println(idWork.snowflakeId());
        System.out.println(idWork.snowflakeId(0, 2));
    }
}
