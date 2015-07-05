package po;

import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

/**
 * <pre>
 * 
 * Executor (update, query, flushStatements, commit, rollback, getTransaction, close, isClosed)
 * ParameterHandler (getParameterObject, setParameters)
 * ResultSetHandler (handleResultSets, handleOutputParameters)
 * StatementHandler (prepare, parameterize, batch, update, query)
 * </pre>
 * 
 * @author Administrator
 * 
 */
// 查看Executor.class,对应的方法名，所需要的参数,其中,insert/update/delete 都归udate方法
/*
 * @Intercepts(@Signature(type = Executor.class, method = "update", args = {
 * MappedStatement.class, Object.class }))
 */
@Intercepts(@Signature(type = Executor.class, method = "query", args = {
		MappedStatement.class, Object.class, RowBounds.class,
		ResultHandler.class }))
public class ExecutorPlugin implements Interceptor {

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		System.out.println(invocation.getMethod().getName());
		System.out.println("ExecutorPlugin.intercept");
		return invocation.proceed();
	}

	@Override
	public Object plugin(Object target) {
		System.out.println("ExecutorPlugin.plugin");
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {
		System.out.println("ExecutorPlugin.setProperties");
	}

}
