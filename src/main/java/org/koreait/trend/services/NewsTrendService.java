package org.koreait.trend.services;

import lombok.RequiredArgsConstructor;
import org.koreait.global.configs.PythonProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;

@Lazy
@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(PythonProperties.class)
public class NewsTrendService {
    private final PythonProperties properties;
    private final WebApplicationContext ctx;

    public void process() { // 매개변수에 주소 들어감
        // spring.profiles.active = default, prod
        boolean isProduction = Arrays.stream(ctx.getEnvironment().getActiveProfiles()).anyMatch(s -> s.equals("prod"));
        try {
            ProcessBuilder builder = new ProcessBuilder();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
