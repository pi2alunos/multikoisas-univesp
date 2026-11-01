package org.multikoisas.repository.jdbcImpl;

import java.util.List;
import java.util.Map;

import org.multikoisas.entities.CategoriaProdutoEntity;
import org.multikoisas.repository.CategoriaProdutoRepository;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CategoriaProdutoRepositoryJdbcImpl implements CategoriaProdutoRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private static final String SELECT_ALL = "SELECT \"categoriaId\", \"produtoId\" FROM public.\"categoriaProduto\"";
    private static final String INSERT = "INSERT INTO public.\"categoriaProduto\" (\"categoriaId\", \"produtoId\") VALUES (:categoriaId, :produtoId)";
    private static final String DELETE_BY_CATEGORIA = "DELETE FROM public.\"categoriaProduto\" WHERE \"categoriaId\" = :categoriaId";
    private static final String DELETE_BY_PRODUTO = "DELETE FROM public.\"categoriaProduto\" WHERE \"produtoId\" = :produtoId";
    private static final String VERIFY = "SELECT COUNT(*) FROM public.\"categoriaProduto\" WHERE \"categoriaId\" = :categoriaId AND \"produtoId\" = :produtoId";

    private static final RowMapper<CategoriaProdutoEntity> ROW_MAPPER = (rs, rowNum) -> {

        CategoriaProdutoEntity categoriaProdutoEntity = new CategoriaProdutoEntity();
        categoriaProdutoEntity.setCategoriaId(rs.getInt("categoriaId"));
        categoriaProdutoEntity.setProdutoId(rs.getInt("produtoId"));
        return categoriaProdutoEntity;

    };

    @Override
    public List<CategoriaProdutoEntity> findAll() {
        return jdbcTemplate.query(SELECT_ALL, ROW_MAPPER);
    }

    public List<CategoriaProdutoEntity> findByProdutoId(Integer produtoId) {
        String SELECT_BY_PRODUCT = SELECT_ALL + " WHERE \"produtoId\" IN (:produtoId);";
        return jdbcTemplate.query(SELECT_BY_PRODUCT, Map.of("produtoId", produtoId), ROW_MAPPER)
                .stream()
                .map(entity ->CategoriaProdutoEntity.builder()
                    .categoriaId(entity.getCategoriaId())
                    .produtoId(entity.getProdutoId())
                    .build())
                .toList();
    }

    @Override
    public List<CategoriaProdutoEntity> findByCategoriaId(Integer categoriaId) {
        String SELECT_BY_PRODUCT = SELECT_ALL + " WHERE \"categoriaId\" IN (:categoriaId);";
        return jdbcTemplate.query(SELECT_BY_PRODUCT, Map.of("categoriaId", categoriaId), ROW_MAPPER)
                .stream()
                .map(entity ->CategoriaProdutoEntity.builder()
                        .categoriaId(entity.getCategoriaId())
                        .produtoId(entity.getProdutoId())
                        .build())
                .toList();
    }

    @Override
    public List<CategoriaProdutoEntity> findByProdutoIdAndCategoriaId (Integer produtoId, Integer categoriaId) {
        String SELECT_BY_PRODUTO_CATEGORIA = SELECT_ALL + " WHERE \"produtoId\" = :produtoId AND \"categoriaId\" = :categoriaId";
        return jdbcTemplate.query(SELECT_BY_PRODUTO_CATEGORIA, Map.of("produtoId",produtoId, "categoriaId", categoriaId), ROW_MAPPER);
    }

    @Override
    public CategoriaProdutoEntity save(CategoriaProdutoEntity entity) {
        jdbcTemplate.update(INSERT, Map.of("categoriaId", entity.getCategoriaId(),"produtoId", entity.getProdutoId()));
        return entity;
    }

    @Override
    public int deleteByProdutoIdAndCategoriaId(Integer produtoId, Integer categoriaId) {
        String DELETE_BY_CATEGORIA_AND_PRODUTO = "DELETE FROM public.\"categoriaProduto\" WHERE \"produtoId\" = :produtoId AND \"categoriaId\" = :categoriaId";
        return jdbcTemplate.update(DELETE_BY_CATEGORIA_AND_PRODUTO, Map.of("produtoId", produtoId, "categoriaId", categoriaId));
    }

    @Override
    public int deleteByProdutoId(Integer produtoId) {
        return jdbcTemplate.update(DELETE_BY_PRODUTO, Map.of("produtoId", produtoId));
    }

    @Override
    public int deleteByCategoriaId( Integer categoriaId) {
       return jdbcTemplate.update(DELETE_BY_CATEGORIA, Map.of( "categoriaId", categoriaId));
    }

    @Override
    public boolean exists(Integer produtoId, Integer categoriaId) {
        Integer count = jdbcTemplate.queryForObject(VERIFY, Map.of("produtoId", produtoId, "categoriaId", categoriaId), Integer.class);
        return count != null && count > 0;
    }
}