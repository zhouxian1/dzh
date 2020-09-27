package com.geovis.controller;

import com.geovis.annotation.Log;
import com.geovis.entity.SysLog;
import com.geovis.service.SysLogService;
import com.geovis.service.UserService;
import com.geovis.shiro.MySession;
import com.geovis.shiro.MySessionDao;
import com.geovis.util.BaseResult;
import com.geovis.util.HttpStatus;
import com.geovis.util.RedisUtil;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/")
@Api("SSOController相关的api")
public class SSOController {
    private final static Logger _log = LoggerFactory.getLogger(SSOController.class);

    // code key
    private final static String SYS_SERVER_CODE = "sys-server-code";


    @Autowired
    UserService userService;
    @Autowired
    MySessionDao mySessionDao;
    @Autowired
    private SysLogService sysLogService;
    @Autowired
    private  RedisUtil redisUtil;

    @Log(name="认证中心首页",mark="单点登录管理",path="/sso/index")
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(HttpServletRequest request) throws Exception {
        String appid = request.getParameter("appid");
        String backurl = request.getParameter("backurl");
        if (StringUtils.isBlank(appid)) {
            throw new RuntimeException("无效访问！");
        }
        // 判断请求认证系统是否注册
//        SystemExample systemExample = new SystemExample();
//        systemExample.createCriteria()
//                .andNameEqualTo(appid);
//        int count = systemService.countByExample(systemExample);
//        if (0 == count) {
//            throw new RuntimeException(String.format("未注册的系统:%s", appid));
//        }
        return "redirect:/sso/login?backurl=" + URLEncoder.encode(backurl, "utf-8");
    }

    @Log(name="登录",mark="单点登录管理",path="/sso/login",method="get")
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(HttpServletRequest request) {
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        String serverSessionId = session.getId().toString();
        // 判断是否已登录，如果已登录，则回跳
        String code = (String) new RedisUtil().get(SYS_SERVER_CODE + "_" + serverSessionId);

        // code校验值
        if (StringUtils.isNotBlank(code)) {
            // 回跳
            String backurl = request.getParameter("backurl");
            String username = (String) subject.getPrincipal();
            if (StringUtils.isBlank(backurl)) {
                backurl = "/";
            } else {
                if (backurl.contains("?")) {
                    backurl += "&code=" + code + "&username=" + username;
                } else {
                    backurl += "?code=" + code + "&username=" + username;
                }
            }
            _log.debug("认证中心帐号通过，带code回跳：{}", backurl);
            return "redirect:" + backurl;
        }
        return "/sso/login";
    }

    @Log(name="登录",mark="单点登录管理",path="/sso/login",method="post")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public Object login(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String rememberMe = request.getParameter("rememberMe");
        if (StringUtils.isBlank(username)) {
            return new BaseResult(HttpStatus.UNAUTHORIZED,"帐号不能为空！");
        }
        if (StringUtils.isBlank(password)) {
            return new BaseResult(HttpStatus.UNAUTHORIZED, "密码不能为空！");
        }
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        String sessionId = session.getId().toString();
        // 判断是否已登录，如果已登录，则回跳，防止重复登录
        String hasCode =(String)redisUtil.get(SYS_SERVER_CODE + "_" + sessionId);
        // code校验值
        if (StringUtils.isBlank(hasCode)) {
            // 使用shiro认证
            UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, password);
            try {
                if (BooleanUtils.toBoolean(rememberMe)) {
                    usernamePasswordToken.setRememberMe(true);
                } else {
                    usernamePasswordToken.setRememberMe(false);
                }
                subject.login(usernamePasswordToken);
            } catch (UnknownAccountException e) {
                return new BaseResult(HttpStatus.NOT_FOUND, "帐号不存在！");
            } catch (IncorrectCredentialsException e) {
                return new BaseResult(HttpStatus.UNAUTHORIZED, "密码错误！");
            } catch (LockedAccountException e) {
                return new BaseResult(HttpStatus.NOT_ACCEPTABLE, "帐号已锁定！");
            }
            // 更新session状态
            mySessionDao.updateStatus(sessionId, MySession.OnlineStatus.on_line);
            // 默认验证帐号密码正确，创建code
            String code = UUID.randomUUID().toString();
//            // code校验值
            redisUtil.set(SYS_SERVER_CODE + "_" + code, code, (int) subject.getSession().getTimeout() / 1000);
        }
        // 回跳登录前地址
        String backurl = request.getParameter("backurl");
        request.getSession().setAttribute("loginName", username);
        SysLog sysLog = new SysLog();
        sysLog.setUsername(username);
        sysLog.setDescription("登录");
        sysLog.setStartTime(new Date().getTime());
        sysLog.setBasePath("http://127.0.0.1:8080");
        sysLog.setUri("/sso/login");
        sysLog.setUrl("http://127.0.0.1:8080/sso/login");
        sysLog.setMethod("POST");
        StringBuffer parameter = new StringBuffer();
        parameter.append("loginName:").append(username).append(",password:").append(password);
        sysLog.setParameter(parameter.toString());
        sysLogService.insertSelective(sysLog);
        if (StringUtils.isBlank(backurl)) {
            return new BaseResult(HttpStatus.OK, "/");
        } else {
            return new BaseResult(HttpStatus.OK, backurl);
        }
    }

//    @Log(name="校验CODE",mark="单点登录管理",path="/sso/code",method="post")
//    @RequestMapping(value = "/code", method = RequestMethod.POST)
//    @ResponseBody
//    public Object code(HttpServletRequest request) {
//        String codeParam = request.getParameter("code");
//        String code = RedisUtil.get(SYS_SERVER_CODE + "_" + codeParam);
//        if (StringUtils.isBlank(codeParam) || !codeParam.equals(code)) {
//            new BaseResult(HttpStatus.FORBIDDEN, "无效code");
//        }
//        return new BaseResult(HttpStatus.OK, code);
//    }

    @Log(name="退出登录",mark="单点登录管理",path="/sso/logout")
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest request) {
        String loginName = (String) request.getSession().getAttribute("loginName");
        SysLog sysLog = new SysLog();
        sysLog.setUsername(loginName);
        sysLog.setDescription("推出登录");
        sysLog.setStartTime(new Date().getTime());
        sysLog.setBasePath("http://127.0.0.1:8080");
        sysLog.setUri("/sso/logout");
        sysLog.setUrl("http://127.0.0.1:8080/sso/logout");
        sysLog.setMethod("GET");
        StringBuffer parameter = new StringBuffer();
        parameter.append("loginName:").append(loginName);
        sysLog.setParameter(parameter.toString());
        sysLogService.insertSelective(sysLog);

        request.getSession().removeAttribute("loginName");
        // shiro退出登录
        SecurityUtils.getSubject().logout();
        // 跳回原地址
        String redirectUrl = request.getHeader("Referer");
        if (null == redirectUrl) {
            redirectUrl = "/";
        }
        return "redirect:" + redirectUrl;
    }


    @Log(name="删除用户判断",path="/sso/checkIsDel",method="POST")
    @RequestMapping(value = "/checkIsDel", method = RequestMethod.POST)
    @ResponseBody
    public String checkIsDel(String username){
        String flag="0";
        Integer status =  userService.getStatusByUserName(username);
        if(null==status){
            flag="2";
        }else if(0==status){
            flag="1";
        }
        return flag;
    }

}
