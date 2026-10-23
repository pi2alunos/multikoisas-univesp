package org.multikoisas.repository.jdbcImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.multikoisas.entities.CategoriaEntity;
import org.multikoisas.repository.CategoriaRepository;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CategoriaRepositoryJdbcImpl implements CategoriaRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final String SELECT_ALL = "SELECT id, nome FROM public.categoria";

    public CategoriaRepositoryJdbcImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final RowMapper<CategoriaEntity> ROW_MAPPER = (rs, rowNum) -> {
        CategoriaEntity e = new CategoriaEntity();
        e.setId(rs.getInt("id"));
        e.setNome(rs.getString("nome"));
        return e;
    };

    @Override
    public List<CategoriaEntity> findAll() {
        return jdbcTemplate.query(SELECT_ALL, ROW_MAPPER);
    }

    @Override
    public Optional<CategoriaEntity> findById(Integer id) {
        String SELECT_BY_ID = SELECT_ALL + " WHERE id = :id";
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        List<CategoriaEntity> result = jdbcTemplate.query(SELECT_BY_ID, params, ROW_MAPPER);
        return result.stream().findFirst();
    }

    @Override
    public Optional<CategoriaEntity> findByName(String nome) {

        Map<String, Object> params = new HashMap<>();
        params.put("nome", nome);
        String SELECT_BY_NAME = SELECT_ALL + " WHERE nome = :nome";
        List<CategoriaEntity> result = jdbcTemplate.query(SELECT_BY_NAME, params, ROW_MAPPER);
        return result.stream().findFirst();
    }

    @Override
    public CategoriaEntity save(CategoriaEntity entity) {
        String INSERT = "INSERT INTO categoria (nome) VALUES (:nome) RETURNING id";
        Map<String, Object> params = new HashMap<>();
        params.put("nome", entity.getNome());
        Integer id = jdbcTemplate.queryForObject(INSERT, params, Integer.class);
                entity.setId(id);
                return entity;
    }

    @Override
    public boolean update(Integer id, CategoriaEntity entity) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("nome", entity.getNome());
        return jdbcTemplate.update("UPDATE categoria SET nome = :nome WHERE id = :id", params) > 0;
    }

    @Override
    public boolean deleteById(Integer id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        return jdbcTemplate.update("DELETE FROM categoria WHERE id = :id", params) > 0;
    }
}