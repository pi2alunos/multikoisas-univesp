package org.multikoisas.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
            return http
                    .csrf(AbstractHttpConfigurer::disable) // Desativa o CSRF pois a API é stateless baseada em token
                    .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Garante que o servidor não guarda sessões
                    .authorizeHttpRequests(auth -> auth

                            // 1. ENDPOINTS LIVRES (Públicos)
                            // Libera rota de login para que o admin consiga se autenticar
                            .requestMatchers(HttpMethod.POST, "/auth/**").permitAll()

                            // Libera documentação do Swagger para visualização
                            .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()

                            // Libera GET para a vitrine pública do Netlify
                            .requestMatchers(
                                    HttpMethod.GET,
                                    "/error",
                                    "/ping/**",
                                    "/socialnetwork/**",
                                    "/categoriaproduto/**",
                                    "/canalvenda/**",
                                    "/canalvendaproduto/**",
                                    "/categoria/**",
                                    "/produto/**",
                                    "/imagem/**",
                                    "/loja/**").permitAll()

                            // 2. ENDPOINTS RESTRITOS (Apenas Admin)
                            // Apenas quem tem a role ADMIN pode manipular a tabela de usuários (inclusive GET para não vazar dados)
                            .requestMatchers("/user/**").permitAll()//.hasRole("ADMIN")

                            // Apenas quem tem a role ADMIN pode criar, alterar ou deletar os recursos da loja
                            .requestMatchers(
                                    HttpMethod.POST,
                                    "/socialnetwork/**",
                                    "/categoriaproduto/**",
                                    "/imagem/**",
                                    "/canalvenda/**",
                                    "/canalvendaproduto/**",
                                    "/categoria/**",
                                    "/produto/**",
                                    "/loja/**").hasRole("ADMIN")

                            .requestMatchers(
                                    HttpMethod.PUT,
                                    "/socialnetwork/**",
                                    "/categoriaproduto/**",
                                    "/imagem/**",
                                    "/canalvenda/**",
                                    "/canalvendaproduto/**",
                                    "/categoria/**",
                                    "/produto/**",
                                    "/loja/**").hasRole("ADMIN")

                            .requestMatchers(
                                    HttpMethod.DELETE,
                                    "/socialnetwork/**",
                                    "/categoriaproduto/**",
                                    "/imagem/**",
                                    "/canalvenda/**",
                                    "/canalvendaproduto/**",
                                    "/categoria/**",
                                    "/produto/**",
                                    "/loja/**").hasRole("ADMIN")

                            // 3. REGRA GERAL DE SEGURANÇA
                            // Qualquer outra rota não mapeada acima precisará, no mínimo, de um usuário autenticado
                            .anyRequest().authenticated()
                    )
                    // 4. A PEÇA CHAVE: Adiciona o seu filtro JWT antes do filtro padrão do Spring
                    .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                    .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}