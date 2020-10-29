package com;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MetricAspect {
    @Around("@annotation(metricTime)")
    public Object metric(ProceedingJoinPoint joinPoint, MetricTime metricTime) throws Throwable {
        String name = metricTime.value();
        long start = System.currentTimeMillis();
        System.err.println("[Metrics] " + name + ": begin");
        try {
            return joinPoint.proceed();
        } finally {
            long t = System.currentTimeMillis() - start;
            System.err.println("[Metrics] " + name + ": " + t + "ms");
        }
    }
}
