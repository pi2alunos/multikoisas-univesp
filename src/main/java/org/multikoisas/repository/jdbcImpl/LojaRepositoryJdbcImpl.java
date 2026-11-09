package org.multikoisas.repository.jdbcImpl;

import lombok.RequiredArgsConstructor;
import org.multikoisas.entities.LojaEntity;
import org.multikoisas.repository.LojaRepository;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class LojaRepositoryJdbcImpl implements LojaRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final static String SELECT_ALL = "SELECT id, \"nomeLoja\", \"descricaoLoja\" FROM public.loja";
    private final static String SELECT_BY_ID = SELECT_ALL + " WHERE id = :id";
    private final static String SELECT_BY_NAME = SELECT_ALL + " WHERE TRIM(UPPER(\"nomeLoja\")) = TRIM(UPPER(:nomeLoja))";
    private final static String SELECT_BY_ID_AND_NAME = SELECT_BY_ID + " AND TRIM(UPPER(\"nomeLoja\")) = TRIM(UPPER(:nomeLoja))";
    private final static String INSERT = "INSERT INTO public.loja (\"nomeLoja\", \"descricaoLoja\") VALUES (:nomeLoja, :descricaoLoja)";
    private final static String DELETE = "DELETE FROM public.loja WHERE id = :id";
    private final static String UPDATE = "UPDATE public.loja SET \"nomeLoja\" = :nomeLoja, \"descricaoLoja\" = :descricaoLoja WHERE id = :id";

    private static final RowMapper<LojaEntity> ROW_MAPPER_LOJA = (rs, rowNum) -> {
        LojaEntity entity = new LojaEntity();
        entity.setId(rs.getInt("id"));
        entity.setNomeLoja(rs.getString("nomeLoja"));
        entity.setDescricaoLoja(rs.getString("descricaoLoja"));
        return entity;
    };

    @Override
    public List<LojaEntity> findAll(){
        return jdbcTemplate.query(SELECT_ALL, ROW_MAPPER_LOJA);
    }

    @Override
    public List<LojaEntity> findById(Integer id){
        return jdbcTemplate.query(SELECT_BY_ID, Map.of("id", id), ROW_MAPPER_LOJA);
    }

    @Override
    public List<LojaEntity> findByName(String nome){
        return jdbcTemplate.query(SELECT_BY_NAME, Map.of("nomeLoja", nome), ROW_MAPPER_LOJA);
    }

    @Override
    public List<LojaEntity> findByIdAndName(Integer id, String nome){
        return jdbcTemplate.query(SELECT_BY_ID_AND_NAME,Map.of("id", id, "nomeLoja", nome), ROW_MAPPER_LOJA);
    }

    @Override
    public LojaEntity save(LojaEntity entity){
        String INSERT_SQL = INSERT + " RETURNING id";
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);
        Integer id = jdbcTemplate.queryForObject(INSERT_SQL, params,Integer.class);
        entity.setId(id);
        return entity;
    }

    @Override
    public LojaEntity update(LojaEntity entity){
        SqlParameterSource params = new BeanPropertySqlParameterSource(entity);
        jdbcTemplate.update(UPDATE, params);
        return entity;
    }

    @Override
    public void delete(Integer id){
        jdbcTemplate.update(DELETE, Map.of("id", id));
    }
}
