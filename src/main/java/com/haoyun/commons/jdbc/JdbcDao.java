package com.haoyun.commons.jdbc;

import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


public interface JdbcDao {

	public <T> List<T> queryForSimpleList(String sql, Object[] paramVarArgs, Class<T> elementType);
	
	public <T> List<T> queryForList(String sql, Object[] paramVarArgs, Class<T> elementType);
	
	public int queryForInt(String sql, Object[] paramVarArgs);
	
	// public <T> Pagination queryForPagination(String sql, Pagination paramPagination, Object[] paramVarArgs, Class<T> paramClass);
	
	public <T> List<T> query(String sql, Object[] paramVarArgs, Class<?> returnClass);
	
	public List<Map<String, Object>> queryForList(String sql, Object[] paramVarArgs);
	
	public <T extends Serializable> List<T> queryBasic(String sql, Object[] paramVarArgs, Class<T> paramClass);
	
	public <T> T queryForSimpleObject(String sql, Object[] paramVarArgs, Class<T> paramClass);
	
	public <T> T queryForObject(String sql, Object[] paramVarArgs, Class<T> paramClass);
	
	public int update(String sql, Object[] paramVarArgs);
	
	public int[] batchUpdate(String sql, List<Object[]> paramList);
	
	public void execute(String sql);
	
	public <T> List<T> queryForIn(String sql, SqlParameterSource paramSource, RowMapper<T> rowMapper); 
	
	public int update(String sql, PreparedStatementSetter pss);

	public <T> List<T> queryForMap(String sql, RowMapper<T> rowMapper);
}

