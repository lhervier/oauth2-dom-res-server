package com.github.lhervier.domino.oauth.resource.sample;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.github.lhervier.domino.oauth.resource.BaseOauthResourceConfig;

@Configuration
@ComponentScan
@EnableWebMvc
@EnableAspectJAutoProxy(proxyTargetClass=true)
public class SampleResourceConfig extends BaseOauthResourceConfig {

}
