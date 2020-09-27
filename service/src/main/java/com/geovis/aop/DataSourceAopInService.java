package com.geovis.aop;

import com.geovis.db.DataSourceContextHolder;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(-10)   //设置AOP执行顺序(需要在事务之前，否则事务只发生在默认库中)
public class DataSourceAopInService {
    private static Logger log = LoggerFactory.getLogger(com.geovis.aop.DataSourceAopInService.class);

    public DataSourceAopInService(){
        log.info("=============初始化AOP===============");
    }

//    @Pointcut("execution(public * com.geovis.impl.*.*(..))")
//    public void log(){
//    }
//
//    /**
//     *
//     * 获取请求的信息
//     */
//    @Before("log()")
//    public void doBefore(JoinPoint joinPoint){
//
//        String methodName=joinPoint.getSignature().getName();
//        if (methodName.startsWith("get")
//                ||methodName.startsWith("count")
//                ||methodName.startsWith("find")
//                ||methodName.startsWith("list")
//                ||methodName.startsWith("select")
//                ||methodName.startsWith("check")
//                ||methodName.startsWith("submitLogin")
//        ){
//            DataSourceContextHolder.setRead();
//        }else {
//            //切换dataSource
//            DataSourceContextHolder.setWrite();
//        }
//    }


    @Before("execution(public  * com.geovis.impl.*.get*(..)) || execution(public * com.geovis.impl..*.isExist*(..)) " +
            "|| execution(public* com.geovis.impl..*.select*(..)) || execution(public * com.geovis.impl..*.count*(..)) " +
            "|| execution(public * com.geovis.impl..*.list*(..)) || execution(public * com.geovis.impl..*.query*(..))" +
            "|| execution(public * com.geovis.impl..*.find*(..))|| execution(public * com.geovis.impl..*.search*(..))|| execution(* com.geovis.impl..*.*(..))")
    public void setSlaveDataSourceType(JoinPoint joinPoint) {
        DataSourceContextHolder.setRead();
        log.info("=========read, method:" + joinPoint.getSignature().getName());
    }

    @Before("execution(* com.geovis.impl..*.add*(..)) || execution(* com.geovis.impl..*.del*(..))" +
            "||execution(* com.geovis.impl..*.upDate*(..)) || execution(* com.geovis.impl..*.insert*(..))" +
            "||execution(* com.geovis.impl..*.create*(..)) || execution(* com.geovis.impl..*.update*(..))" +
            "||execution(* com.geovis.impl..*.delete*(..)) || execution(* com.geovis.impl..*.remove*(..))" +
            "||execution(* com.geovis.impl..*.save*(..)) || execution(* com.geovis.impl..*.relieve*(..))" +
            "|| execution(* com.geovis.impl..*.edit*(..))")
    public void setMasterDataSourceType(JoinPoint joinPoint) {
        DataSourceContextHolder.setWrite();
        log.info("=========write, method:" + joinPoint.getSignature().getName());
    }

}
