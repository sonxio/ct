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
 * �÷�����Ƶ���GETTER SETTER
 * 
 * @author issac
 * 
 */
public class Invoker {
	/*
	 * @param obj �����Ķ���
	 * 
	 * @param att ����������
	 * 
	 * @param value ���õ�ֵ
	 * 
	 * @param type ����������
	 */
	public static void set(Object obj, String att, Object value, Class<?> type) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException {
		Method met = obj.getClass().getMethod("set" + initStr(att), type);
		met.invoke(obj, value);
	}

	/**
	 * ����get��ͷ��getter
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
			try {
				met = obj.getClass().getMethod("is" + initStr(fieldName));
			} catch (NoSuchMethodException e) {
				// boolean����Ҳ����ʹ��get��ʼ�ģ�������get
				met = obj.getClass().getMethod("get" + initStr(fieldName));
			}
		} else {
			met = obj.getClass().getMethod("get" + initStr(fieldName));
		}
		return met.invoke(obj);
	}

	public static String initStr(String old) { // �����ʵ�����ĸ��д
		String str = old.substring(0, 1).toUpperCase() + old.substring(1);
		//��������
		if("mDefectID".equals(old))
			return "mDefectID";
		return str;
	}

	/**
	 * Ϊ����SETTER GETTER��OBJ����������дһЩ�����õ����ֵ
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
					// ���������ƶϸ�ֵ
					if (f.getName().endsWith("imestamp") || f.getName().endsWith("Time")) {// ����Timestamp��ֵ
						Invoker.set(obj, f.getName(), "1977-02-02 09:12:14.111111111", f.getType());
						continue;
					}
					Invoker.set(obj, f.getName(), prefix + "\t" + f.getName() + " test<>.,?/~`!#$%^&*() a@a.com http://testurl.com {}[]msg ���Ĳ���", f.getType());
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
				// ��Ϊ�Զ����ɵķ���ʱ��ʱ���в������ʹ���ģ���˲��ٴ�ӡstacktrace
				DevLog.error("[Invoker] "+e.getMessage());
			} catch (NoSuchMethodException e) {
				// ��Ϊ�Զ����ɵķ���ʱ��ʱ���в����ڵģ���˲��ٴ�ӡstacktrace
				DevLog.error("[Invoker] "+e.getMessage());
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * <h2>ע�⣺�ڲ����У���������ڶ�ȡDB2 ResultSet������reportObj��reportObjSilently�ȷ���<br/>
	 * �����־�������DB2 RESULTSET���Զ��رա���������Ϊ��<br/>
	 * <br/>
	 * ���Ӧ������DB2 RESULTSET��ȡ��������INVOKER��ط��������־</h2>
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
	 * ��CLASS FIELD�������������ⷴ��Ӱ���ٶȡ�
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
	 * <h2>ע�⣺�ڲ����У���������ڶ�ȡDB2 ResultSet������reportObj��reportObjSilently�ȷ���<br/>
	 * �����־�������DB2 RESULTSET���Զ��رա�ԭ��Ŀǰ����ȷ��<br/>
	 * <br/>
	 * ���Ӧ������DB2 RESULTSET��ȡ��������INVOKER��ط��������־</h2>
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
	 * ���������ض�ǰ׺��FIELD�����б�
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
