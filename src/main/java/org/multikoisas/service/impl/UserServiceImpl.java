package org.multikoisas.service.impl;


import lombok.RequiredArgsConstructor;
import org.multikoisas.dto.UpdateUserRequest;
import org.multikoisas.dto.UserRequest;
import org.multikoisas.dto.UserResponse;
import org.multikoisas.entities.UserEntity;
import org.multikoisas.enums.Role;
import org.multikoisas.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.multikoisas.repository.UserRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

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

        //normalização para evitar problemas de case-sensitive
        String normalizedEmail = request.getEmail().toLowerCase().trim();
        Role normalizeRole = Role.valueOf(request.getRole().toString().trim().toUpperCase());

        //Cria instância do usuário a ser atualizado, caso não exista lança exceção
        UserEntity userToUpdate = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new NoSuchElementException("Usuário não encontrado."));

        // Verifica se o novo email já está em uso por outro usuário
        userRepository.findByEmail(request.getEmail())
                .filter(user -> !user.getId().equals(userToUpdate.getId()))
                .ifPresent(user -> {
                    throw new IllegalArgumentException(
                            "Este e-mail já está em uso por outra conta."
                    );
                });

        // 1. Criptografia da senha (se request presente) e atualiza
        String encryptedPassword = passwordEncoder.encode(request.getPassword());
        userToUpdate.setPassword(encryptedPassword);
        // 2. Valida request e atualiza o email
        if (request.getEmail() != null && !request.getEmail().trim().isEmpty()) {
            userToUpdate.setEmail(normalizedEmail);
        }
        // 3. Valida request e atualiza o role
        if(request.getRole() != null && !normalizeRole.toString().trim().isEmpty()) {
            userToUpdate.setRole(normalizeRole);
        }
        // 4. Gravação e Retorno
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
