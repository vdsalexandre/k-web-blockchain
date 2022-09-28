//package com.vds.wishow.kwebblockchain.security
//
//import org.springframework.boot.web.servlet.ServletRegistrationBean
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import org.springframework.security.config.Customizer
//import org.springframework.security.config.annotation.web.builders.HttpSecurity
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
//import org.springframework.security.web.SecurityFilterChain
//import javax.servlet.Servlet
//
//
//@Configuration
//@EnableWebSecurity
//class SecurityConfig {
//
////    companion object {
////        const val USER_LOGGED = "user_logged"
////        const val USER_NOT_LOGGED = "user_not_logged"
////    }
////
//
//    @Bean
//    fun h2ServletRegistration(): ServletRegistrationBean<Servlet> {
//        val registrationBean = ServletRegistrationBean<Servlet>()
//        registrationBean.addUrlMappings("/wicoin-db/*")
//        return registrationBean
//    }
//
//    @Bean
//    fun filterChain(http: HttpSecurity): SecurityFilterChain {
//        return http
//            .csrf { csrf -> csrf.disable() }
//            .authorizeRequests { auth ->
//                auth.antMatchers("/").permitAll()
//                auth.antMatchers("/login").permitAll()
//                auth.antMatchers("/home").authenticated()
//            }
//            .httpBasic(Customizer.withDefaults())
//            .build()
//    }
////
////    @Bean
////    fun userDetailsService(): InMemoryUserDetailsManager {
////        val userDetails = User
////            .builder()
////            .roles(USER_LOGGED)
////            .build()
////
////        return InMemoryUserDetailsManager(userDetails)
////    }
//}