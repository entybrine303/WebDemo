package java6.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java6.interceptor.AuthenticationInterceptor;

@Configuration
public class AuthenticationInterceptorConfig implements WebMvcConfigurer{
	@Autowired
	private AuthenticationInterceptor authenticationInterceptor;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(authenticationInterceptor)
		.addPathPatterns("/admin/**");
		
		registry.addInterceptor(authenticationInterceptor)
		.addPathPatterns("/user/**");
	}
}
