package top.archiesean.oauth.service.impl;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.archiesean.oauth.service.ILoginBiz;

import java.util.HashMap;
import java.util.Map;

/**
 * 登录业务
 *
 * @author ArchieSean
 * @since 2022-07-28 23:21
 */
@Service
@Slf4j
public class LoginBiz implements ILoginBiz {
    /**
     * 用户登录
     *
     * @return 令牌数据
     */
    @Override
    public Map<String, String> login() {
        HashMap<String, String> map = Maps.newHashMap();
        map.put("token", "123");
        return map;
    }
}
