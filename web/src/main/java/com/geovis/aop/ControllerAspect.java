package com.geovis.aop;

import com.geovis.annotation.Log;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;


/**
 * aop切面日志管理
 * @author songgm
 *
 */
@Aspect //该注解标示该类为切面类
@Component //注入依赖
public class ControllerAspect {
	
	private static Logger logger = LoggerFactory.getLogger(ControllerAspect.class);
	
	//@Pointcut("@annotation(com.exp.annotation.Log)")
	public void anyMethod() {

	}
	//标注该方法体为后置通知，当目标方法执行成功后执行该方法体  
	/**
	 * @AfterReturning("within(com.ods.business.controller..*) && @annotation(rl)")注解，
	 * 是指定该方法体为后置通知，其有一个表达式参数，用来检索符合条件的切点。该表达式指定com.ods.business.controller目录下及其所有子目录下的所有带有@Log注解的方法体为切点。
	 * @param jp
	 */
   // @After("anyMethod()")
    public void addLogSuccess(JoinPoint jp){
    	//获取request，可以拿到用户信息
    	HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes()).getRequest();
        //Object[] parames = jp.getArgs();//获取目标方法体参数  
        //String params = parseParames(parames); //解析目标方法体的参数  
        String className = jp.getTarget().getClass().toString();//获取目标类名  
        className = className.substring(className.indexOf("com"));  
        //String signature = jp.getSignature().toString();//获取目标方法签名  
        
        //String methodName = signature.substring(signature.lastIndexOf(".")+1, signature.indexOf("(")); 
        MethodSignature ms=(MethodSignature) jp.getSignature();
        Method method=ms.getMethod();
        Log log = method.getAnnotation(Log.class);
        if (log != null) {
        	String userId = request.getParameter("userId"); // 用户
    		String models = request.getParameter("models"); // 机型
    		String source = request.getParameter("source"); // 访问来源
    		String OSVersion = request.getParameter("OSVersion"); // 系统版本
    		String name = log.name(); // 中文名称
    		String meName = log.method(); // 方法名称
    		String path = log.path(); // 路径
    		String mark = log.mark(); // 备注
    		if (StringUtils.isNotEmpty(userId) && StringUtils.isNotEmpty(models)
					&& StringUtils.isNotEmpty(source)) {
				try {
					String json = "{\"userId\":\"" + userId + "\",\"models\":\""+ models +"\",\"source\":\""+ source + "\",\"name\":\""+ name + "\",\"methodName\":\"" + meName + "\",\"path\":\""+ path + "\",\"mark\":\"" + mark + "\",\"OSVersion\":\""+OSVersion+"\"}";
					logger.info(json);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					logger.error(e.getMessage());
					// e.printStackTrace();
				}
			} else {
				
			}
		}
        
        //String modelName = getModelName(className); //根据类名获取所属的模块  
        //System.out.println("here");
    }  
  
    //标注该方法体为异常通知，当目标方法出现异常时，执行该方法体  
//    @AfterThrowing(pointcut="within(com.abchina.irms..*) && @annotation(rl)", throwing="ex")  
//    public void addLog(JoinPoint jp, Log rl, RsException ex){  
//       
//    }  
}
