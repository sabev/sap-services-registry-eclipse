/*******************************************************************************
 * Copyright (c) 2012 SAP AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     SAP AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.servicesregistry.testutils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Reflection utility to be used inside of testing code.
 * 
 * @author Joerg Dehmel
 */
public final class ReflectionUtils
{
	private ReflectionUtils()
	{
		// creation prohibited
	}

	/**
	 * Provides the value of a field.
	 * 
	 * @param instance
	 *            instance that contains the field
	 * @param fieldName
	 *            name of the field
	 * @return field value, null if field not set in the instance
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public static Object getFieldValue(final Object instance, final String fieldName) throws NoSuchFieldException, IllegalArgumentException,
									IllegalAccessException
	{
		assert instance != null && fieldName != null;

		final Field field = getField(instance.getClass(), fieldName);
		field.setAccessible(true);
		return field.get(instance);
	}

	/**
	 * Sets a new value of an instance field. This method doesn't only considers fields declared in the passed instance but also those declared in the
	 * whole super-hierarchy of the instance.
	 * 
	 * @param instance
	 *            the object thats field value has to be set
	 * @param fieldName
	 *            the name of the instance variable
	 * @param fieldValue
	 *            the new value to be set
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static void setFieldValue(final Object instance, final String fieldName, final Object fieldValue) throws SecurityException,
									NoSuchFieldException, IllegalArgumentException, IllegalAccessException
	{
		assert instance != null && fieldValue != null && fieldName != null;

		final Field field = getField(instance.getClass(), fieldName);
		field.setAccessible(true);
		field.set(instance, fieldValue);
	}
	
	/**
	 * Sets a new value of a static field. This method doesn't only considers fields declared in the passed instance but also those declared in the
	 * whole super-hierarchy of the instance.
	 * 
	 * @param clazz
	 *            the class thats static field value has to be set
	 * @param fieldName
	 *            the name of the static field
	 * @param fieldValue
	 *            the new value to be set
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static void setFieldValue(final Class<? extends Object> clazz, final String fieldName, final Object fieldValue) throws SecurityException,
									NoSuchFieldException, IllegalArgumentException, IllegalAccessException
	{
		
		assert clazz != null && fieldName != null;

		final Field field = getField(clazz, fieldName);
		field.setAccessible(true);
		field.set(null, fieldValue);
	}
	
	private static Field getField(final Class<?> instanceClass, final String fieldName) throws NoSuchFieldException
	{
		assert fieldName != null;

		try
		{
			return instanceClass.getDeclaredField(fieldName);
		} catch (NoSuchFieldException e)
		{
			final Class<?> superClass = instanceClass.getSuperclass();
			if (superClass != null)
			{
				return getField(superClass, fieldName);
			}
			throw e;
		}
	}

	public static Object invokeMethod(final Object instance, final String methodName, final Object[] parameters) throws NoSuchMethodException,
									IllegalArgumentException, IllegalAccessException, InvocationTargetException
	{
		assert instance != null && methodName != null && parameters != null;

		return invokeMethod(instance, methodName, createParameterTypes(parameters), parameters);
	}

	public static Object invokeMethod(final Object instance, final String methodName, final Class<?>[] parTypes, final Object[] parameters)
									throws NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException
	{
		assert instance != null && methodName != null && parTypes != null && parameters != null;

		final Method method = getMethod(instance.getClass(), methodName, parTypes, parameters);
		method.setAccessible(true);
		return method.invoke(instance, parameters);
	}

	private static Method getMethod(final Class<? extends Object> clazz, final String methodName, final Class<?>[] parTypes, final Object[] parameters)
									throws NoSuchMethodException
	{
		assert clazz != null && methodName != null && parTypes != null && parameters != null;

		try
		{
			return clazz.getDeclaredMethod(methodName, parTypes);
		} catch (NoSuchMethodException e)
		{
			final Class<?> superClass = clazz.getSuperclass();
			if (superClass != null)
			{
				return getMethod(superClass, methodName, parTypes, parameters);
			}
			throw e;
		}
	}

	private static Class<?>[] createParameterTypes(final Object[] parameters)
	{
		assert parameters != null;
		final List<Class<?>> types = new ArrayList<Class<?>>();

		for (final Object object : parameters)
		{
			types.add(object.getClass());
		}
		return types.toArray(new Class[types.size()]);
	}

	public static Object newInstance(final Class<?> clazz, final Object[] parameters) throws ClassNotFoundException, SecurityException,
									NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException,
									InvocationTargetException
	{
		assert clazz != null && parameters != null;
		return newInstance(clazz, createParameterTypes(parameters), parameters);
	}

	public static Object newInstance(final Class<?> clazz, final Class<?>[] parTypes, final Object[] parameters) throws ClassNotFoundException,
									SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException,
									IllegalAccessException, InvocationTargetException
	{
		assert clazz != null && parTypes != null && parameters != null;
		final Constructor<?> constr = clazz.getDeclaredConstructor(parTypes);
		constr.setAccessible(true);
		return constr.newInstance(parameters);
	}
}
