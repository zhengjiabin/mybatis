package handler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

public class StringTypeHandler<T> implements TypeHandler<T> {

	@Override
	public void setParameter(PreparedStatement ps, int i, T parameter,
			JdbcType jdbcType) throws SQLException {
		System.out.println("setParameter: " + (String)parameter + " ,jdbcType: " + jdbcType.TYPE_CODE);
		ps.setString(i, (String)parameter);
	}

	@SuppressWarnings("unchecked")
	@Override
	public T getResult(ResultSet rs, String columnName) throws SQLException {
		System.out.println("getResult: " + columnName);  
        return (T) rs.getString(columnName); 
	}

	@SuppressWarnings("unchecked")
	@Override
	public T getResult(ResultSet rs, int columnIndex) throws SQLException {
		T t = (T) rs.getString(columnIndex);
		System.out.println("getResult: " + t);
		return t;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T getResult(CallableStatement cs, int columnIndex)
			throws SQLException {
		System.out.println("getResult " + columnIndex);
		return (T) cs.getString(columnIndex);
	}

}
