package pl.wtrymiga.taskmanager.web.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserIdHeaderFilterConfig {
	@Bean
	FilterRegistrationBean<UserIdHeaderFilter> userIdHeader() {
		var bean = new FilterRegistrationBean<>(new UserIdHeaderFilter());
		bean.addUrlPatterns("/*");
		bean.setOrder(0);
		return bean;
	}
}
