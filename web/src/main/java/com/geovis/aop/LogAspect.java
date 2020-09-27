package com.geovis.aop;

import com.geovis.annotation.Log;
import com.geovis.entity.SysLog;
import org.apache.commons.lang.ObjectUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * 日志记录AOP实现
 */
@Aspect
public class LogAspect {

	private static Logger _log = LoggerFactory.getLogger(LogAspect.class);

	// 开始时间
	private long startTime = 0L;
	// 结束时间
	private long endTime = 0L;

	@Autowired
	//SysLogService sysLogService;

	@Before("execution(* *..controller..*.*(..))")
	public void doBeforeInServiceLayer(JoinPoint joinPoint) {
		_log.debug("doBeforeInServiceLayer");
		startTime = System.currentTimeMillis();
	}

	@After("execution(* *..controller..*.*(..))")
	public void doAfterInServiceLayer(JoinPoint joinPoint) {
		_log.debug("doAfterInServiceLayer");
	}

	@Around("execution(* *..controller..*.*(..))")
	public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
		// 获取request
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
		HttpServletRequest request = servletRequestAttributes.getRequest();

		SysLog sysLog = new SysLog();
		// 从注解中获取操作名称、获取响应结果
		Object result = pjp.proceed();
		Signature signature = pjp.getSignature();
		MethodSignature methodSignature = (MethodSignature) signature;
		Method method = methodSignature.getMethod();
		if (method.isAnnotationPresent(Log.class)) {
			Log log = method.getAnnotation(Log.class);
			sysLog.setDescription(log.name());
		}
		if (method.isAnnotationPresent(RequiresPermissions.class)) {
			RequiresPermissions requiresPermissions = method.getAnnotation(RequiresPermissions.class);
			String[] permissions = requiresPermissions.value();
			if (permissions.length > 0) {
				sysLog.setPermissions(permissions[0]);
			}
		}
		endTime = System.currentTimeMillis();
		_log.debug("doAround>>>result={},耗时：{}", result, endTime - startTime);

//		sysLog.setBasePath(RequestUtil.getBasePath(request));
//		sysLog.setIp(RequestUtil.getIpAddr(request));
		sysLog.setMethod(request.getMethod());
		if (request.getMethod().equalsIgnoreCase("GET")) {
			sysLog.setParameter(request.getQueryString());
		} else {
			sysLog.setParameter(ObjectUtils.toString(request.getParameterMap()));
		}
		sysLog.setResult(ObjectUtils.toString(result));
		sysLog.setSpendTime((int) (endTime - startTime));
		sysLog.setStartTime(startTime);
		sysLog.setUri(request.getRequestURI());
		sysLog.setUrl(ObjectUtils.toString(request.getRequestURL()));
		sysLog.setUserAgent(request.getHeader("User-Agent"));
		sysLog.setUsername(ObjectUtils.toString(request.getUserPrincipal()));
		//sysLogService.insert(sysLog);
		return result;
	}

}
