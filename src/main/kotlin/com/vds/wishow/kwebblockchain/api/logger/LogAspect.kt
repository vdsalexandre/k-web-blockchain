package com.vds.wishow.kwebblockchain.api.logger

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Aspect
@Component
class LogAspect {
    private val logger = LoggerFactory.getLogger(LogAspect::class.java)

    @Around("execution(* com.vds.wishow.kwebblockchain.api.resource.*.*(..))")
    fun logExecution(joinPoint: ProceedingJoinPoint): Any {
        val start = System.currentTimeMillis()
        val resource = joinPoint.signature.declaringTypeName.split(".").last()
        val method = joinPoint.signature.name
        val url = extractUrlFrom(joinPoint)

        val httpVerb = extractHttpVerb(joinPoint)
        val toLog = "Verb: [$httpVerb] method: [$resource.$method()] url: [$url] -"
        val result = try {
            joinPoint.proceed()
        } catch (throwable: Throwable) {
            logger.error("*** Exception during executing ${joinPoint.signature.toShortString()} - ${throwable.message}", throwable)
            throw throwable
        }
        val duration = System.currentTimeMillis() - start
        logger.info("$toLog duration: [$duration ms] - return: $result")
        return result
    }

    private fun extractHttpVerb(joinPoint: ProceedingJoinPoint): String {
        return (joinPoint.signature as MethodSignature)
            .method
            .annotations
            .first()
            .annotationClass
            .simpleName!!
            .substringBefore("Mapping")
    }

    private fun extractUrlFrom(joinPoint: ProceedingJoinPoint): String {
        return (joinPoint.signature as MethodSignature)
            .method.declaredAnnotations.first()
            .toString()
            .substringAfter("value={")
            .substringBefore("}, consumes")
    }
}
