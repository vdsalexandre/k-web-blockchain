package com.vds.wishow.kwebblockchain.security

//@Configuration
//@EnableWebSecurity
//class SecurityConfig {
//
//    @Bean
//    fun filterChain(http: HttpSecurity): SecurityFilterChain {
//        return http
//            .csrf().disable()
//            .authorizeRequests()
//            .antMatchers("/", "/js/**", "/css/**", "/im/**", "/wicoin-db/**").permitAll()
//            .antMatchers("/home").authenticated()
//            .and()
//            .formLogin()
//                .loginPage("/login")
//                .permitAll()
//            .and()
//            .build()
//    }
//}
