package dev.jinulab.isetdx.config.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RestControllerImplAspect extends dev.jinulab.framework.aspect.RestControllerImplAspect{
	@Around("execution(* dev.jinulab.isetdx.hfd.controller.*Impl.*(..))")
	public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
		return super.doAround(pjp);
	}

}
