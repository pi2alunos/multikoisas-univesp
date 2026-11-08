package org.multikoisas.service.impl;


import lombok.RequiredArgsConstructor;
import org.multikoisas.dto.UpdateUserRequest;
import org.multikoisas.dto.UserRequest;
import org.multikoisas.dto.UserResponse;
import org.multikoisas.entities.CategoriaEntity;
import org.multikoisas.entities.UserEntity;
import org.multikoisas.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.multikoisas.repository.UserRepository;

import org.springframework.security.access.AccessDeniedException;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<UserResponse> getUsers(Integer id, String username) {
        List<UserEntity> entity;
        if(id != null) {
            entity = userRepository.findById(id).stream().toList();
        } else if (username != null && !username.trim().isEmpty()) {
            entity = userRepository.findByUsernameIgnoreCase(username).stream().toList();
        } else {
            entity = userRepository.findAll();
            entity.sort(Comparator.comparing(UserEntity::getId));
        }
        return entity.stream().map(this::toResponse).toList();
    }

    @Override
    public List<UserResponse> findAll() {
        return userRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }
    
    @Override
    public UserResponse findById(Integer id) {
        return userRepository.findById(id).map(this::toResponse).orElse(null);
    }

    @Override
    public UserResponse findByUsername(String username) {
        return userRepository.findByUsernameIgnoreCase(username).map(this::toResponse).orElse(null);
    }

    @Override
    public UserResponse save(UserRequest request) {

        String normalizedUsername = request.getUsername().trim();
        String normalizedEmail = request.getEmail().toLowerCase().trim();

        userRepository.findByUsernameIgnoreCase(normalizedUsername).ifPresent(user -> {
            throw new IllegalArgumentException("Este nome de usuário já está em uso.");
        });

        // Se o retorno do banco estiver presente, a exceção é lançada imediatamente
        userRepository.findByEmail(normalizedEmail).ifPresent(user -> {
            throw new IllegalArgumentException("Já existe um usuário cadastrado com este e-mail.");
        });

        String encryptedPassword = passwordEncoder.encode(request.getPassword());

        UserEntity entityToSave = UserEntity.builder()
                .email(normalizedEmail)
                .password(encryptedPassword) // <-- Inserir o hash aqui
                .username(normalizedUsername)
                .role(request.getRole())
                .build();

        UserEntity savedEntity = userRepository.save(entityToSave);
        return toResponse(savedEntity);
    }

    @Override
    public UserResponse delete(String username) {

        UserEntity entityDeleted = userRepository.delete(username);

        if (entityDeleted == null) {
            return null;
        }

        return toResponse(entityDeleted);

    }

    @Override
    public UserResponse update(String username, UpdateUserRequest request) {

        // 1. Identifica o usuário autenticado no contexto do Spring Security
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assert auth != null;
        String loggedUsername = auth.getName();
        boolean isLoggedUserAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        // 2. Bloqueia se um usuário comum tentar editar a conta de outra pessoa
        if (!isLoggedUserAdmin && !loggedUsername.equalsIgnoreCase(username.trim())) {
            throw new AccessDeniedException("Você só tem permissão para alterar apenas o seu email e a senha.");
        }

        // 3. Busca o usuário a ser atualizado
        UserEntity userToUpdate = userRepository.findByUsernameIgnoreCase(username.trim())
                .orElseThrow(() -> new NoSuchElementException("Usuário não encontrado."));

        // 4. Valida e atualiza o e-mail
        if (request.getEmail() != null && !request.getEmail().trim().isEmpty()) {
            String normalizedEmail = request.getEmail().toLowerCase().trim();

            userRepository.findByEmail(normalizedEmail)
                    .filter(user -> !user.getId().equals(userToUpdate.getId()))
                    .ifPresent(user -> {
                        throw new IllegalArgumentException("Este e-mail já está em uso por outra conta.");
                    });

            userToUpdate.setEmail(normalizedEmail);
        }

        // 5. Criptografa e atualiza a senha
        if (request.getPassword() != null && !request.getPassword().trim().isEmpty()) {
            userToUpdate.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        // 6. Atualização silenciosa: só altera a role se o usuário logado for ADMIN e o campo existir no request
        if (isLoggedUserAdmin && request.getRole() != null) {
            userToUpdate.setRole(request.getRole());
        }

        // 7. Gravação e Retorno
        userRepository.update(userToUpdate);
        return toResponse(userToUpdate);

    }

    private UserResponse toResponse(UserEntity userEntity) {
        return UserResponse.builder()
                .id(userEntity.getId())
                .email(userEntity.getEmail())
                .username(userEntity.getUsername())
                .role(userEntity.getRole())
                .build();
    }

}
