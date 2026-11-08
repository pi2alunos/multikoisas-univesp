package org.multikoisas.repository.jdbcImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.multikoisas.entities.CanalVendaEntity;
import org.multikoisas.repository.CanalVendaRepository;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class CanalVendaRepositoryJdbcImpl implements CanalVendaRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final String SELECT_ALL = "SELECT id, nome FROM public.\"canalVenda\"";
    private final String INSERT = "INSERT INTO public.\"canalVenda\" (\"nome\") VALUES (:nome)";

    public CanalVendaRepositoryJdbcImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final RowMapper<CanalVendaEntity> ROW_MAPPER = (rs, rowNum) -> {
        CanalVendaEntity e = new CanalVendaEntity();
        e.setId(rs.getInt("id"));
        e.setNome(rs.getString("nome"));
        return e;
    };

    @Override
    public List<CanalVendaEntity> findAll() {
        return jdbcTemplate.query(SELECT_ALL, ROW_MAPPER);
    }

    @Override
    public List<CanalVendaEntity> findByIdAndName(Integer id, String nome) {
        String SELECT_BY_ID_AND_NAME = SELECT_ALL + " WHERE id = :id AND TRIM(LOWER(nome)) = TRIM(LOWER(:nome))";
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("nome", nome);
        return jdbcTemplate.query(SELECT_BY_ID_AND_NAME, params, ROW_MAPPER);
    }

    @Override
    public List<CanalVendaEntity> findByName(String nome) {
        String SELECT_BY_NAME = SELECT_ALL + " WHERE TRIM(LOWER(nome)) = TRIM(LOWER(:nome))";
        return jdbcTemplate.query(SELECT_BY_NAME, Map.of("nome", nome), ROW_MAPPER);
    }

    public List<CanalVendaEntity> findById(Integer id) {
        String SELECT_BY_ID = SELECT_ALL + " WHERE id = :id";
        return jdbcTemplate.query(SELECT_BY_ID, Map.of("id", id), ROW_MAPPER);
    }

    @Override
    public CanalVendaEntity save(CanalVendaEntity entity) {
        // 1. Preparamos o SQL correto para inserção
        String INSERT_SQL = INSERT + " RETURNING id";

        // 2. Lemos todos os atributos do usuário (que vieram preenchidos do POST)
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);

        // 3. Executamos o INSERT de fato e aguardamos o ID gerado como retorno
        Integer generatedId = jdbcTemplate.queryForObject(INSERT_SQL, params, Integer.class);

        // 4. Injetamos o ID no objeto e o devolvemos
        entity.setId(generatedId);

        return entity;
    }

    @Override
    public boolean update(Integer id, CanalVendaEntity entity) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("nome", entity.getNome());
        return jdbcTemplate.update("UPDATE \"canalVenda\" SET nome = :nome WHERE id = :id", params) > 0;
    }

    @Override
    public boolean deleteById(Integer id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        return jdbcTemplate.update("DELETE FROM \"canalVenda\" WHERE id = :id", params) > 0;
    }
}
