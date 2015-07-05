package util;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 反射工具
 * 
 * <pre>
 * 1、设置属性
 *      PropertyDescriptor pd = new PropertyDescriptor(fieldName, c);
 *      
 *      //调用setter        
 *      Method writeMethod = pd.getWriteMethod();    //setName()
 *      writeMethod.invoke(obj, "value");
 *      
 *      //调用getter
 *      Method readMethod = pd.getReadMethod();        //getName()
 *      readMethod.invoke(obj);
 * 2、getMethod()/getDeclaredMethod()
 *      getMethod()：只能访问类中声明为公有的方法,私有的方法它无法访问,能访问从其它类继承来的公有方法
 *      getDeclaredFields()：能访问类中所有的字段,与public,private,protect无关,不能访问从其它类继承来的方法
 *      
 * 3、getField()/getDeclareField()
 *      getField()：只能访问类中声明为公有的字段,私有的字段它无法访问，能访问从其它类继承来的公有方法
 *      getDeclareField()：能访问类中所有的字段,与public,private,protect无关,不能访问从其它类继承来的方法
 *      
 * 4、getConstructors()/getDeclaredConstructors()
 *      getConstructors()：只能访问类中声明为public的构造函数
 *      getDeclaredConstructors()：能访问类中所有的构造函数,与public,private,protect无关
 * </pre>
 * 
 * @author Administrator
 * 
 */
public class ReflectUtil {
    
    /**
     * 根据字段名反射调用setter方法
     * 
     * <pre>
     * 1、只能访问类中声明为公有的字段,私有的字段它无法访问，能访问从其它类继承来的公有方法
     * 2、boolean类型：boolean isTest->setTest()/isTest()
     * </pre>
     * 
     * @param obj
     * @param fieldName
     * @param value
     * @return
     */
    public static void invokeSetterMethod(Object obj, String fieldName, Object value) {
        if (obj == null || fieldName == null) {
            return;
        }
        
        Class<?> c = obj.getClass();
        PropertyDescriptor property = null;
        try {
            property = new PropertyDescriptor(fieldName, c);
            Method setterMethod = property.getWriteMethod();
            setterMethod.invoke(obj, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 根据字段名反射调用getter方法
     * 
     * <pre>
     * 1、只能访问类中声明为公有的字段,私有的字段它无法访问，能访问从其它类继承来的公有方法
     * 2、boolean类型：boolean isTest->setTest()/isTest()
     * </pre>
     * 
     * @param obj
     * @param fieldName
     * @param value
     * @return
     */
    public static Object invokeGetterMethod(Object obj, String fieldName) {
        if (obj == null || fieldName == null) {
            return null;
        }
        
        Class<?> c = obj.getClass();
        
        Object value = null;
        PropertyDescriptor property = null;
        try {
            property = new PropertyDescriptor(fieldName, c);
            Method setterMethod = property.getReadMethod();
            value = setterMethod.invoke(obj, fieldName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return value;
    }
    
    /**
     * 获取对象的方法(tomcat模式)
     * 
     * @param obj
     * @param methodName
     * @param parameterTypes
     * @return
     */
    public static Method getMethod(Object obj, String methodName, Class<?>... parameterTypes) {
        if (obj == null || methodName == null) {
            return null;
        }
        
        // 先从当前类中查找
        Class<?> c = obj.getClass();
        Method method = getMethodByCurrent(c, methodName, parameterTypes);
        if (method != null) {
            return method;
        }
        
        // 委派父类查找
        return getMethodByDelegate(c, methodName, parameterTypes);
    }
    
    /**
     * 当前Class查找指定方法
     * 
     * @param c
     * @param methodName
     * @param parameterTypes
     * @return
     */
    private static Method getMethodByCurrent(Class<?> c, String methodName, Class<?>... parameterTypes) {
        Method[] methods = c.getDeclaredMethods();
        for (Method method : methods) {
            if (isAssignMethodByMethodName(method, methodName) && isSameParameterTypes(method, parameterTypes)) {
                return method;
            }
        }
        
        return null;
        
        /*//方法二
        Method method = null;
        try {
            method = c.getDeclaredMethod(methodName, parameterTypes);
        } catch (Exception e) {
            //无需抛异常，为的就是委派获取指定方法
        } 
        
        return method;*/
    }
    
    /**
     * 是否指定方法名的方法
     * 
     * @param method
     * @param methodName
     * @return
     */
    private static boolean isAssignMethodByMethodName(Method method, String methodName) {
        if (methodName.equals(method.getName())) {
            return true;
        }
        
        return false;
    }
    
    /**
     * Class参数是否相同
     * 
     * @param method
     * @param parameterTypes
     * @return
     */
    private static boolean isSameParameterTypes(Method method, Class<?>... parameterTypes) {
        Class<?>[] methodParameterTypes = method.getParameterTypes();
        
        if (methodParameterTypes == null && parameterTypes == null) {
            return true;
        }
        
        if (methodParameterTypes == null && parameterTypes != null) {
            return false;
        }
        
        if (methodParameterTypes != null && parameterTypes == null) {
            return false;
        }
        
        if (methodParameterTypes.length != parameterTypes.length) {
            return false;
        }
        
        for (int i = 0; i < methodParameterTypes.length; i++) {
            if (methodParameterTypes[i] != parameterTypes[i]) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * 委派模式查找指定方法
     * 
     * @param c
     * @param methodName
     * @param parameterTypes
     * @return
     */
    private static Method getMethodByDelegate(Class<?> c, String methodName, Class<?>... parameterTypes) {
        Class<?> superClass = c.getSuperclass();
        if (superClass == null) {
            return getMethodByCurrent(c, methodName, parameterTypes);
        }
        
        Method method = getMethodByDelegate(c, methodName, parameterTypes);
        if (method == null) {
            return getMethodByCurrent(c, methodName, parameterTypes);
        }
        
        return method;
    }
    
    /**
     * 利用反射获取指定对象里面的指定属性(tomcat模式查找)
     * 
     * @param obj 目标对象
     * @param fieldName 目标属性
     * @return 目标字段
     * @throws IntrospectionException
     */
    public static Field getField(Object obj, String fieldName) {
        if (obj == null || fieldName == null) {
            return null;
        }
        
        // 先从当前类中查找
        Class<?> c = obj.getClass();
        Field field = getFieldByCurrent(c, fieldName);
        if (field != null) {
            return field;
        }
        
        // 委派父类查找
        return getFieldByDelegate(c, fieldName);
    }
    
    /**
     * 当前Class查找指定字段
     * 
     * @param c
     * @param fieldName
     * @return
     */
    private static Field getFieldByCurrent(Class<?> c, String fieldName) {
        Field[] fields = c.getDeclaredFields();
        for (Field field : fields) {
            if (fieldName.equals(field.getName())) {
                return field;
            }
        }
        
        return null;
        
        /*//方法二
        Field field = null;
        try {
            field = c.getDeclaredField(fieldName);
        } catch (Exception e) {
            //无需抛异常，为的就是能够委派模式获取指定字段
        } 
        
        return field;*/
    }
    
    /**
     * 委派模式查找指定字段
     * 
     * @param c
     * @param fieldName
     * @return
     */
    private static Field getFieldByDelegate(Class<?> c, String fieldName) {
        Class<?> superClass = c.getSuperclass();
        if (superClass == null) {
            return getFieldByCurrent(c, fieldName);
        }
        
        Field field = getFieldByDelegate(superClass, fieldName);
        if (field == null) {
            return getFieldByCurrent(c, fieldName);
        }
        
        return field;
    }
    
    /**
     * 利用反射获取指定对象的指定属性
     * 
     * @param obj 目标对象
     * @param fieldName 目标属性
     * @return 目标属性的值
     */
    public static Object getFieldValue(Object obj, String fieldName) {
        Field field = getField(obj, fieldName);
        if (field == null) {
            return null;
        }
        
        // 设置属性是可以访问的
        field.setAccessible(true);
        
        Object result = null;
        
        try {
            // 得到此属性的值
            result = field.get(obj);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        
        return result;
    }
    
    /**
     * 利用反射设置指定对象的指定属性为指定的值
     * 
     * @param obj 目标对象
     * @param fieldName 目标属性
     * @param fieldValue 目标值
     */
    public static void setFieldValue(Object obj, String fieldName, String fieldValue) {
        Field field = ReflectUtil.getField(obj, fieldName);
        if (field == null) {
            return;
        }
        
        field.setAccessible(true);
        
        try {
            field.set(obj, fieldValue);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
