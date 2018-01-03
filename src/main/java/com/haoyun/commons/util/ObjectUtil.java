package com.haoyun.commons.util;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ObjectUtil {

	public static  <T> Map<String, Object> convert2Map(T t){
        Map<String, Object> result = new HashMap<>();
        Method[] methods = t.getClass().getMethods();

        try {
            for (Method method : methods){
                Class<?>[] paramClass = method.getParameterTypes();
                if(paramClass.length > 0){ // 如果方法带参数，则跳过
                    continue;
                }
                String methodName = method.getName();
                if(methodName.startsWith("get") && !methodName.contains("getClass")){
                    Object value = method.invoke(t);
                    String str= methodName.substring(3, methodName.length());
                    String first = str.substring(0,1).toLowerCase();
                    String rest = str.substring(1, str.length());
                    String newStr = new StringBuffer(first).append(rest).toString();
                    result.put(newStr, value);
                }
            }

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 转大写
     * @param ch
     * @return
     */
    private static char charToUpperCase(char ch){
        if(ch <= 122 && ch >= 97){
            ch -= 32;
        }
        return ch;
    }

    /**
     * 转小写
     * @param ch
     * @return
     */
    private static char charToLowerCase(char ch){
        if(ch <= 90 && ch >= 65){
            ch += 32;
        }
        return ch;
    }
}
