package org.multikoisas.repository.jdbcImpl;

import java.sql.Types;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.multikoisas.entities.UserEntity;
import org.multikoisas.enums.Role;
import org.multikoisas.repository.UserRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRepositoryJdbcImpl implements UserRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private static final String SELECT_ALL = "SELECT id, email, password, username, role FROM public.usuario";
    private static final String SELECT_BY_ID = SELECT_ALL + " WHERE id = :id";
    private static final String UPDATE_USER = "UPDATE public.usuario SET email = :email, password = :password, role = :role WHERE TRIM(LOWER(username)) = TRIM(LOWER(:username))";
    private static final String SELECT_BY_USERNAME = SELECT_ALL + " WHERE TRIM(LOWER(username)) = TRIM(LOWER(:username))";
    private static final String INSERT = "INSERT INTO public.usuario (email, password, username, role) VALUES (:email, :password, :username, :role)";
    private static final String DELETE_BY_ID = "DELETE FROM public.usuario WHERE id = :id";


    private static final RowMapper<UserEntity> ROW_MAPPER = (rs, rowNum) -> {

        UserEntity userEntity = new UserEntity();
        userEntity.setId(rs.getInt("id"));
        userEntity.setEmail(rs.getString("email"));
        userEntity.setPassword(rs.getString("password"));
        userEntity.setUsername(rs.getString("username"));
        userEntity.setRole(Role.valueOf(rs.getString("role")));

        return userEntity;

    };

    @Override
    public List<UserEntity> findAll() {

        return jdbcTemplate.query(SELECT_ALL, ROW_MAPPER);

    }

    @Override
    public Optional<UserEntity> findById(Integer id) {

        try {

            UserEntity result = jdbcTemplate.queryForObject(SELECT_BY_ID, Map.of("id", id), ROW_MAPPER);

            return Optional.of(result);

        } catch (EmptyResultDataAccessException e) {

            return Optional.empty();

        }
    }

    @Override
    public Optional<UserEntity> findByEmail(String email) {

        String SELECT_BY_EMAIL = SELECT_ALL + " WHERE TRIM(LOWER(\"email\")) = TRIM(LOWER(:email))";

        List<UserEntity> result = jdbcTemplate.query(SELECT_BY_EMAIL, Map.of("email", email), ROW_MAPPER);

        return result.isEmpty() ? Optional.empty() : Optional.of(result.getFirst());

    }

    @Override
    public Optional<UserEntity> findByUsernameIgnoreCase(String username) {

        List<UserEntity> result = jdbcTemplate.query(SELECT_BY_USERNAME, Map.of("username", username), ROW_MAPPER);

        return result.isEmpty() ? Optional.empty() : Optional.of(result.getFirst());
    }

    @Override
    public UserEntity save(UserEntity entity) {
        // 1. Preparamos o SQL correto para inserção
        String INSERT_USER = INSERT + " RETURNING id";

        // 2. Lemos todos os atributos do usuário (que vieram preenchidos do POST)
        BeanPropertySqlParameterSource params = new BeanPropertySqlParameterSource(entity);
        params.registerSqlType("role", Types.VARCHAR); //importante indicar o tipo do ENUM (que é varchar) para o JDBC

        // 3. Executamos o INSERT de fato e aguardamos o ID gerado como retorno
        Integer generatedId = jdbcTemplate.queryForObject(INSERT_USER, params, Integer.class);

        // 4. Injetamos o ID no objeto e o devolvemos
        entity.setId(generatedId);

        return entity;
    }

    @Override
    public UserEntity delete(String username) {
        //captura o do id do usuario a ser deletado
        List<UserEntity> entity = jdbcTemplate.query(SELECT_BY_USERNAME, Map.of("username", username), ROW_MAPPER);

        //verifica se o usuario existe
        if (entity.isEmpty()) {
            return null;
        }

        UserEntity result = entity.getFirst();

        //retorna o delete do usuario com base no id capturado
        jdbcTemplate.update(DELETE_BY_ID, Map.of("id", result.getId()));

        return result; // retorna o usuario deletado
    }

    @Override
    public void update(UserEntity entity) {
        // O BeanPropertySqlParameterSource mapeia automaticamente os atributos do objeto para os :parametros do SQL
        BeanPropertySqlParameterSource params = new BeanPropertySqlParameterSource(entity);
        params.registerSqlType("role", Types.VARCHAR); //importante indicar o tipo do ENUM (que é varchar) para o JDBC

        jdbcTemplate.update(UPDATE_USER, params);
    }
}
