package com.vds.wishow.kwebblockchain.logger

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class LoggerAOP

private val logger = LoggerFactory.getLogger(LogAspect::class.java)

@Aspect
@Component
class LogAspect {
    @Around("@annotation(com.vds.wishow.kwebblockchain.logger.LoggerAOP)")
    fun logExecution(joinPoint: ProceedingJoinPoint): Any {
        val start = System.currentTimeMillis()
        val resource = joinPoint.staticPart.signature.declaringTypeName.split(".").last()
        val method = joinPoint.staticPart.signature.name
        val url = extractUrlFrom(joinPoint)

        val httpVerb = (joinPoint.signature as MethodSignature).method.annotations[1].annotationClass.simpleName
        val toLog = "$httpVerb on $resource.$method() - requestMapping: $url"
        val result = try {
            joinPoint.proceed()
        } catch (throwable: Throwable) {
            logger.error("*** Exception during executing ${joinPoint.signature.toShortString()}", throwable)
            throw throwable
        }
        val duration = System.currentTimeMillis() - start
        logger.info("$toLog, duration: $duration ms")
        return result
    }

    private fun extractUrlFrom(joinPoint: ProceedingJoinPoint) = (joinPoint.signature as MethodSignature)
        .method.declaredAnnotations[1]
        .toString()
        .substringAfter("value={")
        .substringBefore("}")
}
