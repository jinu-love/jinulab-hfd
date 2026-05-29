package dev.jinulab.isetdx.config.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import dev.jinulab.dbist.aspect.AbstractTransactionAspect;

@Aspect
@Component
public class TransactionAspect extends AbstractTransactionAspect {

	@Around("execution(* dev.jinulab.isetdx.hfd.service.*.*.*(..))")
	public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
		return super.doAround(pjp);
	}

}
