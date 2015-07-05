package util;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * ���乤��
 * 
 * <pre>
 * 1����������
 *      PropertyDescriptor pd = new PropertyDescriptor(fieldName, c);
 *      
 *      //����setter        
 *      Method writeMethod = pd.getWriteMethod();    //setName()
 *      writeMethod.invoke(obj, "value");
 *      
 *      //����getter
 *      Method readMethod = pd.getReadMethod();        //getName()
 *      readMethod.invoke(obj);
 * 2��getMethod()/getDeclaredMethod()
 *      getMethod()��ֻ�ܷ�����������Ϊ���еķ���,˽�еķ������޷�����,�ܷ��ʴ�������̳����Ĺ��з���
 *      getDeclaredFields()���ܷ����������е��ֶ�,��public,private,protect�޹�,���ܷ��ʴ�������̳����ķ���
 *      
 * 3��getField()/getDeclareField()
 *      getField()��ֻ�ܷ�����������Ϊ���е��ֶ�,˽�е��ֶ����޷����ʣ��ܷ��ʴ�������̳����Ĺ��з���
 *      getDeclareField()���ܷ����������е��ֶ�,��public,private,protect�޹�,���ܷ��ʴ�������̳����ķ���
 *      
 * 4��getConstructors()/getDeclaredConstructors()
 *      getConstructors()��ֻ�ܷ�����������Ϊpublic�Ĺ��캯��
 *      getDeclaredConstructors()���ܷ����������еĹ��캯��,��public,private,protect�޹�
 * </pre>
 * 
 * @author Administrator
 * 
 */
public class ReflectUtil {
    
    /**
     * �����ֶ����������setter����
     * 
     * <pre>
     * 1��ֻ�ܷ�����������Ϊ���е��ֶ�,˽�е��ֶ����޷����ʣ��ܷ��ʴ�������̳����Ĺ��з���
     * 2��boolean���ͣ�boolean isTest->setTest()/isTest()
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
     * �����ֶ����������getter����
     * 
     * <pre>
     * 1��ֻ�ܷ�����������Ϊ���е��ֶ�,˽�е��ֶ����޷����ʣ��ܷ��ʴ�������̳����Ĺ��з���
     * 2��boolean���ͣ�boolean isTest->setTest()/isTest()
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
     * ��ȡ����ķ���(tomcatģʽ)
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
        
        // �ȴӵ�ǰ���в���
        Class<?> c = obj.getClass();
        Method method = getMethodByCurrent(c, methodName, parameterTypes);
        if (method != null) {
            return method;
        }
        
        // ί�ɸ������
        return getMethodByDelegate(c, methodName, parameterTypes);
    }
    
    /**
     * ��ǰClass����ָ������
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
        
        /*//������
        Method method = null;
        try {
            method = c.getDeclaredMethod(methodName, parameterTypes);
        } catch (Exception e) {
            //�������쳣��Ϊ�ľ���ί�ɻ�ȡָ������
        } 
        
        return method;*/
    }
    
    /**
     * �Ƿ�ָ���������ķ���
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
     * Class�����Ƿ���ͬ
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
     * ί��ģʽ����ָ������
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
     * ���÷����ȡָ�����������ָ������(tomcatģʽ����)
     * 
     * @param obj Ŀ�����
     * @param fieldName Ŀ������
     * @return Ŀ���ֶ�
     * @throws IntrospectionException
     */
    public static Field getField(Object obj, String fieldName) {
        if (obj == null || fieldName == null) {
            return null;
        }
        
        // �ȴӵ�ǰ���в���
        Class<?> c = obj.getClass();
        Field field = getFieldByCurrent(c, fieldName);
        if (field != null) {
            return field;
        }
        
        // ί�ɸ������
        return getFieldByDelegate(c, fieldName);
    }
    
    /**
     * ��ǰClass����ָ���ֶ�
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
        
        /*//������
        Field field = null;
        try {
            field = c.getDeclaredField(fieldName);
        } catch (Exception e) {
            //�������쳣��Ϊ�ľ����ܹ�ί��ģʽ��ȡָ���ֶ�
        } 
        
        return field;*/
    }
    
    /**
     * ί��ģʽ����ָ���ֶ�
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
     * ���÷����ȡָ�������ָ������
     * 
     * @param obj Ŀ�����
     * @param fieldName Ŀ������
     * @return Ŀ�����Ե�ֵ
     */
    public static Object getFieldValue(Object obj, String fieldName) {
        Field field = getField(obj, fieldName);
        if (field == null) {
            return null;
        }
        
        // ���������ǿ��Է��ʵ�
        field.setAccessible(true);
        
        Object result = null;
        
        try {
            // �õ������Ե�ֵ
            result = field.get(obj);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        
        return result;
    }
    
    /**
     * ���÷�������ָ�������ָ������Ϊָ����ֵ
     * 
     * @param obj Ŀ�����
     * @param fieldName Ŀ������
     * @param fieldValue Ŀ��ֵ
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
