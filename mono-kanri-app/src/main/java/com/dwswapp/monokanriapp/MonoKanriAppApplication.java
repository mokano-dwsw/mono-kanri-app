package com.dwswapp.monokanriapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class MonoKanriAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(MonoKanriAppApplication.class, args);
	}

	@Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NonNull CorsRegistry registry) { // ← メソッド名を修正
                registry.addMapping("/**") // ← registry に対して呼び出す
                        .allowedOrigins(
                            "https://creative-pothos-04ee3c.netlify.app",
                            "http://localhost:5173"
                        )
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*");
            }
        };
    }
}
