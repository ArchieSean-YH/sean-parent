package top.archiesean.oauth.service;

import java.util.Map;

/**
 * 登录服务
 *
 * @author ArchieSean
 * @since 2022-07-28 23:20
 */
public interface ILoginBiz {
    /**
     * 用户登录接口
     *
     * @return map
     */
    Map<String, String> login();
}
