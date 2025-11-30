package com.exercicioPratico.LabProgramacao.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


 
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    
    /**
     * Configura CORS para as rotas da API.
   
     * IMPORTANTE: 
     * - Se o IP da máquina mudar, atualize esta configuração!

     */
    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        registry.addMapping("/api/**")  // Aplica apenas para rotas /api/**
                .allowedOrigins(
                    "http://localhost:3000",           // Expo Web (porta padrão)
                    "http://localhost:8081",           // Alternativa para desenvolvimento web
                    "http://192.168.15.114:3000",      // IP da máquina - Expo Web
                    "http://192.168.15.114:8081"       // IP da máquina - alternativa
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);  // Permite envio de credenciais (cookies, headers de autenticação)
    }
}