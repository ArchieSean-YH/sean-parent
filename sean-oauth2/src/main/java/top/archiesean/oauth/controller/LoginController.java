package top.archiesean.oauth.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import top.archiesean.common.response.Result;
import top.archiesean.oauth.service.ILoginBiz;

import java.util.Map;

/**
 * 登录接口
 *
 * @Author ArchieSean
 * @Date 2022-07-07 21:21
 */
@RestController
public class LoginController {

    private final ILoginBiz iLoginBiz;

    public LoginController(ILoginBiz iLoginBiz) {
        this.iLoginBiz = iLoginBiz;
    }

    /**
     * 用户登录
     *
     * @return map
     */
    @PostMapping("login")
    public Result<Map<String, String>> login() {
        return Result.ok(iLoginBiz.login());
    }
}
