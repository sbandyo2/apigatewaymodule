package com.ibm.gateway;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.StaticHeadersWriter;

@EnableWebSecurity
public class SecurityTokenConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private JwtConfig jwtConfig;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.csrf()
				.disable()
				
				// make sure we use stateless session; session won't be used to
				// store user's state.
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				// handle an authorized attempts
				.exceptionHandling().authenticationEntryPoint((req, rsp, e) -> rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED))
				.and()
				// Add a filter to validate the tokens with every request
				.addFilterAfter(new JwtTokenAuthenticationFilter(jwtConfig),UsernamePasswordAuthenticationFilter.class)
				// authorization requests config
				.authorizeRequests()
				// allow all who are accessing "auth" service
				.antMatchers(HttpMethod.POST, jwtConfig.getUri()).permitAll()
				.antMatchers(HttpMethod.POST, jwtConfig.getUri()).permitAll()
			    .antMatchers(HttpMethod.GET, "/actuator/health").permitAll()
			    .antMatchers(HttpMethod.GET, "/actuator").permitAll()
				// must be an admin if trying to access admin area
				// (authentication is also required here)
				.antMatchers("/ordernow" + "/on-service/**").hasRole("ONUSER")
				.antMatchers("/yourprocure" + "/yourprocure-service/**").hasRole("YPUSER")
				.antMatchers("/csa" + "/csa-service/**").hasRole("CSAUSER")
				.antMatchers("/dwaas" + "/dwaas-service/**").hasRole("DWAASUSER")
				.antMatchers("/deviceibm" + "/deviceibm-service/**").hasRole("DEVICEIBMUSER")
				.antMatchers("/backend-service" + "/backend-service/**").hasRole("DBUSER")
				.antMatchers("/ariba" + "/sapariba-service/**").hasRole("ADMIN")
				// Any other request must be authenticated
				.anyRequest().authenticated()
				.and()
				.headers()
				.addHeaderWriter(new StaticHeadersWriter("Access-Control-Allow-Origin", "*"))
	            .addHeaderWriter(new StaticHeadersWriter("Access-Control-Allow-Methods", "POST, GET"))
	            .addHeaderWriter(new StaticHeadersWriter("Access-Control-Max-Age", "3600"))
	            .addHeaderWriter(new StaticHeadersWriter("Access-Control-Allow-Credentials", "true"))
	            .addHeaderWriter(new StaticHeadersWriter("Access-Control-Allow-Headers", "Origin,Accept,X-Requested-With,Content-Type,Access-Control-Request-Method,Access-Control-Request-Headers,Authorization"));;
	}

	@Bean
	public JwtConfig jwtConfig() {
		return new JwtConfig();
	}
}
