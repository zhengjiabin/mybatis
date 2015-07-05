package po;

import java.util.List;
import java.util.Properties;

import org.apache.ibatis.reflection.factory.DefaultObjectFactory;

public class MybatisObjectFactory extends DefaultObjectFactory {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public <T> T create(Class<T> type) {
		return super.create(type);
	}
	
	@Override
	public <T> T create(Class<T> type, List<Class<?>> constructorArgTypes,
			List<Object> constructorArgs) {
		return super.create(type, constructorArgTypes, constructorArgs);
	}
	
	@Override
	public void setProperties(Properties properties) {
		System.out.println("setProperties: " + properties.toString() + ", someProperty: " + properties.getProperty("someProperty"));  
        super.setProperties(properties);  
	}
}
