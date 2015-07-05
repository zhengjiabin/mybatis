package interceptor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import model.Page;

import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;

import util.ReflectUtil;

/**
 * <pre>
 * ��ҳ������������������Ҫ���з�ҳ��ѯ�Ĳ�����Ȼ�������з�ҳ���� 
 * 
 * ����������ʵ��Mybatis��ҳ��ԭ��
 * Ҫ����JDBC�����ݿ���в����ͱ���Ҫ��һ����Ӧ��Statement����Mybatis��ִ��Sql���ǰ�ͻ����һ������Sql����Statement����
 * �����Ҷ�Ӧ��Sql�������Statement֮ǰ�����ģ��������ǾͿ�����������Statement֮ǰ����������Statement��Sql������֡�
 * 
 * ��Mybatis��Statement�����ͨ��RoutingStatementHandler�����prepare�������ɵġ�
 * ��������������ʵ��Mybatis��ҳ��һ��˼·��������StatementHandler�ӿڵ�prepare������Ȼ���������������а�Sql���ĳɶ�Ӧ�ķ�ҳ��ѯSql���
 * ��֮���ٵ���StatementHandler�����prepare������������invocation.proceed()��
 * 
 * ���ڷ�ҳ���ԣ����������������ǻ���Ҫ����һ����������ͳ�����㵱ǰ�����ļ�¼һ���ж��٣�����ͨ����ȡ����ԭʼ��Sql����
 * ��������Ϊ��Ӧ��ͳ�����������Mybatis��װ�õĲ��������ò����Ĺ��ܰ�Sql����еĲ��������滻��֮����ִ�в�ѯ��¼����Sql�������ܼ�¼����ͳ�ơ�
 * </pre>
 */
@Intercepts({@Signature(method = "prepare", type = StatementHandler.class, args = {Connection.class})})
public class PageInterceptor implements Interceptor {
    
    /** ���ݿ����ͣ���ͬ�����ݿ��в�ͬ�ķ�ҳ���� */
    private String databaseType;
    
    /**
     * ���غ�Ҫִ�еķ���
     * 
     * <pre>
     * 1������StatementHandler��ʵֻ������ʵ���࣬һ����RoutingStatementHandler����һ���ǳ�����BaseStatementHandler
     * 1)��BaseStatementHandler���������࣬�ֱ���SimpleStatementHandler��PreparedStatementHandler��CallableStatementHandler
     * 2)��SimpleStatementHandler�����ڴ���Statement
     * 3)��PreparedStatementHandler�Ǵ���PreparedStatement
     * 4)��CallableStatementHandler�Ǵ���CallableStatement
     * 
     * 2��Mybatis�ڽ���Sql��䴦���ʱ���ǽ�����RoutingStatementHandler������RoutingStatementHandler����ӵ��һ��StatementHandler���͵�delegate����
     * 3��RoutingStatementHandler������Statement�Ĳ�ͬ������Ӧ��BaseStatementHandler����SimpleStatementHandler��PreparedStatementHandler��CallableStatementHandler
     * 4����RoutingStatementHandler��������StatementHandler�ӿڷ�����ʵ�ֶ��ǵ��õ�delegate��Ӧ�ķ���
     * 5��Mybatisֻ���ڽ���RoutingStatementHandler��ʱ����ͨ��Interceptor��plugin�������а�����
     * 
     * ��ҳ�����߼�
     * 1����PageInterceptor������@Signature����˸�Interceptorֻ����StatementHandler�ӿڵ�prepare���������Դ����ص���Ŀ�����϶���RoutingStatementHandler����
     * </pre>
     */
    public Object intercept(Invocation invocation) throws Throwable {
        RoutingStatementHandler handler = (RoutingStatementHandler)invocation.getTarget();
        
        // ͨ�������ȡ����ǰRoutingStatementHandler�����delegate����
        StatementHandler delegate = (StatementHandler)ReflectUtil.getFieldValue(handler, "delegate");
        
        // ��ȡ����ǰStatementHandler��boundSql�����ﲻ���ǵ���handler.getBoundSql()����ֱ�ӵ���delegate.getBoundSql()�����һ����
        // ��Ϊ֮ǰ�Ѿ�˵����RoutingStatementHandlerʵ�ֵ�����StatementHandler�ӿڷ������涼�ǵ��õ�delegate��Ӧ�ķ�����
        BoundSql boundSql = delegate.getBoundSql();
        
        // �õ���ǰ��Sql�Ĳ������󣬾��������ڵ��ö�Ӧ��Mapperӳ�����ʱ������Ĳ�������
        Object obj = boundSql.getParameterObject();
        
        // �������Ǽ򵥵�ͨ���������Page������϶�������Ҫ���з�ҳ�����ġ�
        if (obj instanceof Page<?>) {
            this.handlePage(invocation, delegate, boundSql, (Page<?>)obj);
        }
        return invocation.proceed();
    }
    
    /**
     * �����ҳ
     * 
     * @param invocation
     * @param delegate
     * @param boundSql
     * @param obj
     */
    private void handlePage(Invocation invocation, StatementHandler delegate, BoundSql boundSql, Page<?> page) {
        // ͨ�������ȡdelegate����BaseStatementHandler��mappedStatement����
        MappedStatement mappedStatement = (MappedStatement)ReflectUtil.getFieldValue(delegate, "mappedStatement");
        
        // ���ص���prepare����������һ��Connection����
        Connection connection = (Connection)invocation.getArgs()[0];
        
        // ��ȡ��ǰҪִ�е�Sql��䣬Ҳ��������ֱ����Mapperӳ�������д��Sql���
        String sql = boundSql.getSql();
        
        // ����ǰ��page�������������ܼ�¼��
        this.setTotalRecord(page, mappedStatement, connection);
        
        // ��ȡ��ҳSql���
        String pageSql = this.getPageSql(page, sql);
        
        // ���÷������õ�ǰBoundSql��Ӧ��sql����Ϊ���ǽ����õķ�ҳSql���
        ReflectUtil.setFieldValue(boundSql, "sql", pageSql);
    }
    
    /**
     * ��������Ӧ�ķ�װԭʼ����ķ���
     */
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }
    
    /**
     * ����ע��������ʱ�趨������
     */
    public void setProperties(Properties properties) {
        this.databaseType = properties.getProperty("databaseType");
    }
    
    /**
     * ����page�����ȡ��Ӧ�ķ�ҳ��ѯSql��䣬����ֻ�����������ݿ����ͣ�Mysql��Oracle ���������ݿⶼ û�н��з�ҳ
     * 
     * @param page ��ҳ����
     * @param sql ԭsql���
     * @return
     */
    private String getPageSql(Page<?> page, String sql) {
        StringBuffer sqlBuffer = new StringBuffer(sql);
        if ("mysql".equalsIgnoreCase(databaseType)) {
            return getMysqlPageSql(page, sqlBuffer);
        } else if ("oracle".equalsIgnoreCase(databaseType)) {
            return getOraclePageSql(page, sqlBuffer);
        }
        return sqlBuffer.toString();
    }
    
    /**
     * ��ȡMysql���ݿ�ķ�ҳ��ѯ���
     * 
     * @param page ��ҳ����
     * @param sqlBuffer ����ԭsql����StringBuffer����
     * @return Mysql���ݿ��ҳ���
     */
    private String getMysqlPageSql(Page<?> page, StringBuffer sqlBuffer) {
        // �����һ����¼��λ�ã�Mysql�м�¼��λ���Ǵ�0��ʼ�ġ�
        int offset = (page.getPageNumber() - 1) * page.getPageSize();
        sqlBuffer.append(" limit ").append(offset).append(",").append(page.getPageSize());
        
        return sqlBuffer.toString();
    }
    
    /**
     * ��ȡOracle���ݿ�ķ�ҳ��ѯ���
     * 
     * @param page ��ҳ����
     * @param sqlBuffer ����ԭsql����StringBuffer����
     * @return Oracle���ݿ�ķ�ҳ��ѯ���
     */
    private String getOraclePageSql(Page<?> page, StringBuffer sqlBuffer) {
        // �����һ����¼��λ�ã�Oracle��ҳ��ͨ��rownum���еģ���rownum�Ǵ�1��ʼ��
        // select * from (select u.*, rownum r from (select * from t_user) u where rownum < 31) where r >= 16
        int offset = (page.getPageNumber() - 1) * page.getPageSize() + 1;
        sqlBuffer.insert(0, "select u.*, rownum r from (").append(") u where rownum < ").append(offset + page.getPageSize());
        sqlBuffer.insert(0, "select * from (").append(") where r >= ").append(offset);
        
        return sqlBuffer.toString();
    }
    
    /**
     * ����ǰ�Ĳ�������page�����ܼ�¼��
     * 
     * @param page Mapperӳ������Ӧ�Ĳ�������
     * @param mappedStatement Mapperӳ�����
     * @param connection ��ǰ�����ݿ�����
     */
    private void setTotalRecord(Page<?> page, MappedStatement mappedStatement, Connection connection) {
        // ��ȡ��Ӧ��BoundSql�����BoundSql��ʵ����������StatementHandler��ȡ����BoundSql��ͬһ������
        // delegate�����boundSqlҲ��ͨ��mappedStatement.getBoundSql(paramObj)������ȡ���ġ�
        BoundSql boundSql = mappedStatement.getBoundSql(page);
        
        // ��ȡ�������Լ�д��Mapperӳ������ж�Ӧ��Sql���
        String sql = boundSql.getSql();
        
        // ͨ����ѯSql����ȡ����Ӧ�ļ����ܼ�¼����sql���
        String countSql = this.getCountSql(sql);
        
        // ͨ��BoundSql��ȡ��Ӧ�Ĳ���ӳ��
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        
        // ����Configuration����ѯ��¼����Sql���countSql������ӳ���ϵparameterMappings�Ͳ�������page������ѯ��¼����Ӧ��BoundSql����
        BoundSql countBoundSql = new BoundSql(mappedStatement.getConfiguration(), countSql, parameterMappings, page);
        
        // ͨ��mappedStatement����������page��BoundSql����countBoundSql����һ�������趨������ParameterHandler����
        ParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement, page, countBoundSql);
        
        // ͨ��connection����һ��countSql��Ӧ��PreparedStatement����
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(countSql);
            
            // ͨ��parameterHandler��PreparedStatement�������ò���
            parameterHandler.setParameters(pstmt);
            
            // ֮�����ִ�л�ȡ�ܼ�¼����Sql���ͻ�ȡ����ˡ�
            rs = pstmt.executeQuery();
            if (rs.next()) {
                int totalRecord = rs.getInt(1);
                
                // ����ǰ�Ĳ���page���������ܼ�¼��
                page.setTotalRecordAndTotalPage(totalRecord);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * ����ԭSql����ȡ��Ӧ�Ĳ�ѯ�ܼ�¼����Sql���
     * 
     * @param sql
     * @return
     */
    private String getCountSql(String sql) {
        int index = sql.indexOf("from");
        return "select count(*) " + sql.substring(index);
    }
    
}
