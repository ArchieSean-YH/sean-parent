package top.archiesean.snowflake.service;

/**
 * 雪花id服务
 *
 * @author ArchieSean
 * @date 2022-07-12 17:11
 */
public interface ISnowflake {

    /**
     * 获取雪花id
     *
     * @return long
     */
     Long nextId();
}
