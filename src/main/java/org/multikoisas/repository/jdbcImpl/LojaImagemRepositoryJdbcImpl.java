package org.multikoisas.repository.jdbcImpl;

import lombok.RequiredArgsConstructor;
import org.multikoisas.entities.LojaImagemEntity;
import org.multikoisas.enums.LojaImagemTipo;
import org.multikoisas.repository.LojaImagemRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public  class LojaImagemRepositoryJdbcImpl implements LojaImagemRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final String SELECT_ALL = "SELECT id, \"imagemUrl\", \"lojaImagemTipo\" FROM public.\"lojaImagem\"";
    private final String SELECT_BY_ID = SELECT_ALL + " WHERE id = :id";
    private final String SELECT_BY_ID_AND_TIPO = SELECT_BY_ID + " AND TRIM(UPPER(\"lojaImagemTipo\")) = TRIM(UPPER(:lojaImagemTipo))";
    private final String SELECT_BY_TIPO = SELECT_ALL + "WHERE TRIM(UPPER(\"lojaImagemTipo\")) = TRIM(UPPER(:lojaImagemTipo))";
    private final String INSERT = "INSERT INTO public.\"lojaImagem\" (\"imagemUrl\", \"lojaImagemTipo\") VALUES (:imagemUrl, :tipo)";
    private final String DELETE_BY_ID_AND_TIPO = "DELETE FROM public.\"lojaImagem\" WHERE id = id AND TRIM(UPPER(\"lojaImagemTipo\")) = TRIM(UPPER(:lojaImagemTipo))";


    private final RowMapper<LojaImagemEntity> ROW_MAPPER = (rs , rowNum) -> {
        LojaImagemEntity lojaImagemEntity = new LojaImagemEntity();
        lojaImagemEntity.setId(rs.getInt("id"));
        lojaImagemEntity.setImagemUrl(rs.getString("imagemUrl"));
        lojaImagemEntity.setTipo(LojaImagemTipo.valueOf(rs.getString(("lojaImagemTipo"))));
        return lojaImagemEntity;
    };

    @Override
    public List<LojaImagemEntity> findAll() {
        return jdbcTemplate.query(SELECT_ALL, ROW_MAPPER);
    }


    @Override
    public List<LojaImagemEntity> findByIdAndTipo(Integer id, String lojaImagemTipo) {
        return jdbcTemplate.query(SELECT_BY_ID_AND_TIPO, Map.of("id", id, "lojaImagemTipo", lojaImagemTipo), ROW_MAPPER);
    }

    @Override
    public List<LojaImagemEntity> findById(Integer id) {
        try {
            return jdbcTemplate.query(
                    SELECT_BY_ID,
                    Map.of("id", id),
                    ROW_MAPPER
            );
        } catch (EmptyResultDataAccessException e) {
            return null; // Opcional: lançar uma exceção de domínio (ex: EntityNotFoundException)
        }
    }
    @Override
    public List<LojaImagemEntity> findByTipo(String lojaImagemTipo){
        return jdbcTemplate.query(SELECT_BY_TIPO, Map.of("lojaImagemTipo", lojaImagemTipo), ROW_MAPPER);
    }

    @Override
    public void deleteByIdAndTipo(Integer id, String lojaImagemTipo) {
        jdbcTemplate.update(DELETE_BY_ID_AND_TIPO, Map.of("id", id, "lojaImagemTipo", lojaImagemTipo));
    }


    @Override
    public LojaImagemEntity save(LojaImagemEntity entity) {
        String INSERT_IMAGEM = INSERT + " RETURNING id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("imagemUrl", entity.getImagemUrl());
        params.addValue("tipo",entity.getTipo().name());
        Integer generatedId = jdbcTemplate.queryForObject(INSERT_IMAGEM, params, Integer.class);
        entity.setId(generatedId);
        return entity;
    }

}
