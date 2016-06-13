package com.ibm.issac.toolkit5.param;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.ibm.issac.toolkit.Cube;
import com.ibm.issac.toolkit.DevLog;
import com.ibm.issac.toolkit.app.MapCache;
import com.ibm.issac.toolkit.param.SysProp;

/**
 * 用反射机制调用GETTER SETTER
 * 
 * @author issac
 * 
 */
public class Invoker {
	/*
	 * @param obj 操作的对象
	 * 
	 * @param att 操作的属性
	 * 
	 * @param value 设置的值
	 * 
	 * @param type 参数的类型
	 */
	public static void set(Object obj, String att, Object value, Class<?> type) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException {
		Method met = obj.getClass().getMethod("set" + initStr(att), type);
		met.invoke(obj, value);
	}

	/**
	 * 返回get开头的getter
	 * 
	 * @param obj
	 * @param fieldName
	 * @param fieldType
	 * @return
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static Object get(Object obj, String fieldName, Class<?> fieldType) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Method met;
		if (fieldType.equals(boolean.class) || fieldType.equals(Boolean.class)) {
			try{
			met = obj.getClass().getMethod("is" + initStr(fieldName));
			}catch(NoSuchMethodException e){
				//boolean类型也可能使用get开始的，尝试用get
				met = obj.getClass().getMethod("get" + initStr(fieldName));	
			}
		} else {
			met = obj.getClass().getMethod("get" + initStr(fieldName));
		}
		return met.invoke(obj);
	}

	public static String initStr(String old) { // 将单词的首字母大写
		String str = old.substring(0, 1).toUpperCase() + old.substring(1);
		return str;
	}

	/**
	 * 为带有SETTER GETTER的OBJ各个属性填写一些测试用的随机值
	 * 
	 * @param obj
	 */
	public static void autoSetter(Object obj, String prefix) {
		final Field[] fields = Invoker.getCachedDeclaredFields(obj.getClass());
		for (Field f : fields) {
			try {
				if (f.getType().equals(boolean.class) || f.getType().equals(Boolean.class)) {
					Invoker.set(obj, f.getName(), true, f.getType());
					continue;
				}
				if (f.getType().equals(String.class)) {
					// 根据名称推断赋值
					if (f.getName().endsWith("imestamp") || f.getName().endsWith("Time")) {// 按照Timestamp赋值
						Invoker.set(obj, f.getName(), "1977-02-02 09:12:14.111111111", f.getType());
						continue;
					}
					Invoker.set(obj, f.getName(), prefix + "\t" + f.getName() + " test<>.,?/~`!#$%^&*() a@a.com http://testurl.com {}[]msg 中文测试", f.getType());
					continue;
				}
				if (f.getType().equals(Integer.class) || f.getType().equals(Long.class) || f.getType().equals(long.class) || f.getType().equals(int.class)) {
					Invoker.set(obj, f.getName(), Cube.produceRandomInteger(0, 100), f.getType());
					continue;
				}
				// DevLog.trace("Field " + f.getName() + " of type " + f.getType().toString() + " is not set.");
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * <h2>注意：在测试中，发现如果在读取DB2 ResultSet当中用reportObj或reportObjSilently等方法<br/>
	 * 输出日志，会造成DB2 RESULTSET被自动关闭。可能是因为。<br/>
	 * <br/>
	 * 因此应避免在DB2 RESULTSET读取过程中用INVOKER相关方法输出日志</h2>
	 * 
	 * @param obj
	 * @return
	 */
	public static String reportObj(Object obj) {
		if (!SysProp.b_bool("issac.debugMode.invoker", true)) {
			return "";
		}
		StringBuffer sb = new StringBuffer(obj.getClass().getSimpleName());
		sb.append('{');
		final Field[] fields = Invoker.getCachedDeclaredFields(obj.getClass());
		for (Field f : fields) {
			try {
				Object valObj = Invoker.get(obj, f.getName(), f.getType());
				sb.append(f.getName()).append(":>").append(valObj == null ? "$NULL" : valObj).append("<, ");
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		sb.append('}');
		return sb.toString();
	}

	/**
	 * 把CLASS FIELD缓存起来，避免反射影响速度。
	 * 
	 * @param cls
	 * @return
	 */
	public static Field[] getCachedDeclaredFields(Class<? extends Object> cls) {
		final String cacheId = cls.getCanonicalName();
		Field[] fA = (Field[]) MapCache.ins.get(cacheId);
		if (fA != null) {
			return fA;
		}
		fA = cls.getDeclaredFields();
		MapCache.ins.put(cacheId, fA);
		return fA;
	}

	/**
	 * <h2>注意：在测试中，发现如果在读取DB2 ResultSet当中用reportObj或reportObjSilently等方法<br/>
	 * 输出日志，会造成DB2 RESULTSET被自动关闭。原理目前不明确。<br/>
	 * <br/>
	 * 因此应避免在DB2 RESULTSET读取过程中用INVOKER相关方法输出日志</h2>
	 * 
	 * @param obj
	 * @return
	 */
	public static String reportObjSilently(Object obj) {
		if (!SysProp.b_bool("issac.debugMode.invoker", true)) {
			return "";
		}
		final StringBuffer sb = new StringBuffer(obj.getClass().getSimpleName());
		sb.append('{');
		final Field[] fields = obj.getClass().getDeclaredFields();
		for (Field f : fields) {
			try {
				sb.append(f.getName()).append(":\"").append(Invoker.get(obj, f.getName(), f.getType())).append("\", ");
			} catch (SecurityException e) {
			} catch (IllegalArgumentException e) {
			} catch (NoSuchMethodException e) {
			} catch (IllegalAccessException e) {
			} catch (InvocationTargetException e) {
			}
		}
		sb.append('}');
		return sb.toString();
	}

	/**
	 * 给出带有特定前缀的FIELD名称列表
	 * 
	 * @param fieldPrefix
	 * @param cls
	 * @return
	 */
	public static List<String> listFieldsWithPrefix(String fieldPrefix, Class<? extends Object> cls) {
		final Field[] fA = Invoker.getCachedDeclaredFields(cls);
		final List<String> l = new ArrayList<String>();
		for (Field f : fA) {
			if (f.getName().startsWith(fieldPrefix)) {
				l.add(f.getName());
			}
		}
		return l;
	}
}
