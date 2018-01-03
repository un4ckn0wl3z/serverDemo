package com.haoyun.commons.jdbc;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Repository
public class JdbcDaoImpl implements JdbcDao {

	private JdbcTemplate jdbcTemplate;

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public <T> List<T> queryForSimpleList(String sql, Object[] paramVarArgs,
			Class<T> elementType) {

		try {
			return this.jdbcTemplate.queryForList(sql, elementType, paramVarArgs);
		} catch (Exception e) {
		}
		return null;
	}

	@Override
	public <T> List<T> queryForList(String sql, Object[] paramVarArgs,
			Class<T> elementType) {

		try {
			RowMapper<T> rm = BeanPropertyRowMapper.newInstance(elementType);
			return this.jdbcTemplate.query(sql, paramVarArgs, rm);
		} catch (Exception e) {
		}
		return null;
	}

	@Override
	public int queryForInt(String sql, Object[] paramVarArgs) {
		return this.jdbcTemplate.queryForInt(sql, paramVarArgs);
	}

	@Override
	public <T> List<T> query(String sql, Object[] paramVarArgs,
			Class<?> returnClass) {
		return (List<T>) this.jdbcTemplate.query(sql, paramVarArgs, BeanPropertyRowMapper.newInstance(returnClass));
	}

	@Override
	public <T extends Serializable> List<T> queryBasic(String sql,
			Object[] paramVarArgs, Class<T> paramClass) {
		return this.jdbcTemplate.queryForList(sql, paramClass, paramVarArgs);
	}

	@Override
	public <T> T queryForSimpleObject(String sql, Object[] paramVarArgs,
			Class<T> paramClass) {

		try {
			return this.jdbcTemplate.queryForObject(sql, paramVarArgs, paramClass);
		} catch (Exception e) {
		}
		return null;
	}

	@Override
	public <T> T queryForObject(String sql, Object[] paramVarArgs,
			Class<T> paramClass) {

		try {
			RowMapper<T> rm = BeanPropertyRowMapper.newInstance(paramClass);
			List<T> list = this.jdbcTemplate.query(sql, paramVarArgs, rm);
			if(list!=null && list.size()>0){
				return list.get(0);
			}
		} catch (Exception e) {
		}
		return null;
	}

	@Override
	public int update(String sql, Object[] paramVarArgs) {
		return this.jdbcTemplate.update(sql, paramVarArgs);
	}

	@Override
	public int update(String sql, PreparedStatementSetter pss) {
		return this.jdbcTemplate.update(sql, pss);
	}

	@Override
	public int[] batchUpdate(String sql, List<Object[]> paramList) {
		return this.jdbcTemplate.batchUpdate(sql, paramList);
	}

	@Override
	public void execute(String sql) {
		this.jdbcTemplate.execute(sql);
	}

	@Override
	public List<Map<String, Object>> queryForList(String sql,
			Object[] paramVarArgs) {
		return this.jdbcTemplate.queryForList(sql, paramVarArgs);
	}

	/**
	 * for sql in
	 */
	@Override
	public <T> List<T> queryForIn(String sql, SqlParameterSource paramSource, RowMapper<T> rowMapper) {

		NamedParameterJdbcTemplate namedJdbcTemplate = new NamedParameterJdbcTemplate(this.jdbcTemplate);
		return namedJdbcTemplate.query(sql, paramSource, rowMapper);
	}

	/**
	 * for sql map
	 * 元素需要遍历，再查询或执行复杂逻辑
	 */
	@Override
	public <T> List<T> queryForMap(String sql, RowMapper<T> rowMapper) {

		NamedParameterJdbcTemplate namedJdbcTemplate = new NamedParameterJdbcTemplate(this.jdbcTemplate);
		return namedJdbcTemplate.query(sql, rowMapper);
	}


}

