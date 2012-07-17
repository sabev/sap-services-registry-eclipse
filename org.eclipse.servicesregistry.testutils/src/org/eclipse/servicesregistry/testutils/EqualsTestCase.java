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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

import java.lang.reflect.Modifier;
import java.util.Iterator;

import org.junit.Test;

/**
 * An abstract test case for the <c>Object.equals(Object)</c> and <c>Object.hashCode()</c> methods. This test case performs the tests for <c>equals</c>
 * and <c>hashCode</c> simultaniesly as they are tightly connected. The tescase test the following properties of equals/hasCode:
 * <p>
 * 1. equals reflexivity 2. equals symetricity 3. equals transitivyte 4. hashCode statbility 5. hashCode immutability 6. equals is suitable for
 * subclasses
 * </p>
 * 
 * The test cases is abstract and need to be sublcassed by concrete test case. Several abstract methods need to be implemented by concrete test cases.
 * 
 * @author Hristo Sabev
 * 
 * @param <T>
 */
public abstract class EqualsTestCase<T>
{

	private final Class<T> testClass;

	/**
	 * Creates a new <c>EqualsTestCase</c> instance to test the equals method of the supplied class
	 * 
	 * @param testClass -
	 *            the class whose equals method will be tested
	 */
	public EqualsTestCase(Class<T> testClass)
	{
		this(null, testClass);
	}

	/**
	 * Creates a new <c>EqualsTestCase</c> instance to test the equals method of the supplied class. This test case also takes a name paramter, which
	 * i passed to <c>TestCase</c> super constructor. I.e. this is the name of the test case
	 * 
	 * @param name -
	 *            name of the test case
	 * @param testClass -
	 *            the class whose equals method will be tested
	 */
	public EqualsTestCase(String name, Class<T> testClass)
	{
		assertNotNull("test class passed in constructor was null", testClass);
		this.testClass = testClass;
	}

	/**
	 * Obtains a new iterator of instances for the tested class.
	 * 
	 * @param iterations -
	 *            number of iterations i.e the number of times one can invoke <c>Iterator.next()</c> withouth getting <c>NoSuchElementException</c>
	 *            exception
	 * @return a new iterator of instanced for the tested class;
	 */
	abstract public Iterator<T> newObjectIterator(int iterations);

	/**
	 * Obtains a new instance of the tested class. Each call to this method should return a new instance, which is considered equal to the instance
	 * returned by the prefvious call but is not identical. For example if the tested class was <c>String</c> the most obvious implementation of this
	 * method would be <c>
	 * <p>
	 * return new String("test string")
	 * </p>
	 * </c>
	 * 
	 * @return a new instance of the test class assumed to be equal to the previously returned instances
	 */
	abstract public T newEqualInstance();

	/**
	 * Obtains a new instance of a single class, which is a subclass of the tested class. The subclass must not override the <c>equals</c> method of
	 * the tested class. All instances should be equal. The returned instances should have the same values of all the inherited fields as the
	 * instances retured by <c>newEqualInstance</c>
	 * 
	 * @return returns a new instance of single sublcass of the tested class. Each call should return an instance equal to the previously returned but
	 *         not identical. Null if the tested class is final
	 */
	abstract public T newAncestorEqualInstance();

	/**
	 * Obtains a new instance of the the tested class. The single restriction on the returned instances is that they should not be equal to the
	 * instances returned by <c>newEqualInstance</c>
	 * 
	 * @return a new instance of the test class. The newly returned instance is not equal to the instances returned by <c>newEqualInstance</c>
	 */
	abstract public T newNonEqualInstance();

	/**
	 * Modifies a given instance of the tested class. The instance should be modified in such a way that it's not equal to the instances retured by
	 * <c>newEqualInstance</c> ans <c>newNoneEqualInstance()</c>. If the tested class is immutable then the implementation of this method should be
	 * empty
	 * 
	 * @param instance -
	 *            the instance to be modified.
	 */
	abstract public void modifyObjectInstance(T instance);

	/**
	 * Obtains the number of comparisons that will be made for the reflexivity test. This method can be overriden by subclasses to specify a different
	 * value. By default this number is 100.
	 * 
	 * @return - the number of comparisons that will be made for the reflexivity test.
	 */
	public int getIterationsForReflixivityTest()
	{
		return 100;
	}

	/**
	 * Obatains the number of comparisons that will be made for the symitricity test. This method can be override by subclasses to specify a different
	 * value. By default this number is 1000
	 * 
	 * @return - the number of comparisons that will be made for the reflixivity test.
	 */
	public int getIterationsForSymetricityTest()
	{
		return 1000;
	}

	/**
	 * Obatains the number of comparisons that will be made for the transitivity test. This method can be override by subclasses to specify a
	 * different value. By default this number is 2000
	 * 
	 * @return - the number of comparisons that will be made for the transitivity test.
	 */
	public int getIterationsForTransitivityTest()
	{
		return 2000;
	}

	/**
	 * Tests that equals is reflexive. It enforces the rule a.equals(a) == true. This test also tests that hashCode function is stable(). I.e.
	 * hashCode doesn't change it's value between several invocations, as well as that hashCode is immutable over object change. The test strategy is
	 * to create an new object iterator and then for each returned instance:
	 * 
	 * <ol>
	 * <li>Compare it with itself</li>
	 * <li>check that its hash code stays the same</li>
	 * <li>modify the returned instance by calling <c>modifyInstance</c> and then check the hashCode again</li>
	 * </ol>
	 * 
	 */
	@Test
	public void testEqulsReflexive()
	{
		final int iterationsForThisTest = getIterationsForReflixivityTest();
		final Iterator<T> objectTiterator = newObjectIterator(iterationsForThisTest);
		while (objectTiterator.hasNext())
		{
			final T objectT = objectTiterator.next();
			assertTrue("equals is not reflexive", objectT.equals(objectT));
			final int initialHashCode = objectT.hashCode();
			for (int i = 0; i < iterationsForThisTest; i++)
			{
				assertEquals("hashCode is not stable upon multiple calls", initialHashCode, objectT.hashCode());
			}
			modifyObjectInstance(objectT);
			assertEquals("hashCode is not stable upon modification of the object", initialHashCode, objectT.hashCode());
		}
	}

	/**
	 * Tests that equals is symetric i.e. a.equals(b) <==> b.equals(a). This method also tests that hashCode behaves correctly in sense a.quals(b) =>
	 * a.hashCode() == b.hashCode(). This test uses two object iterators. One for a and one for b, where a and b are from the above rule. Each of
	 * these iterators are instantiated with the same number of elements. This method also checks that whenever two instances returned by the two
	 * iterators are equal then their hascode is equal.
	 * 
	 */
	@Test
	public void testEqualsSymetric()
	{
		final int iterationsForThisTest = getIterationsForSymetricityTest();
		final Iterator<T> objectTIterator1 = newObjectIterator(iterationsForThisTest);
		final Iterator<T> objectTIterator2 = newObjectIterator(iterationsForThisTest);
		while (objectTIterator1.hasNext())
		{
			assertTrue("The two object iterators don't have equal number of objects", objectTIterator2.hasNext());
			final T objectT1 = objectTIterator1.next();
			final T objectT2 = objectTIterator2.next();
			if (objectT1.equals(objectT2))
			{
				assertTrue("Equals is not symetric", objectT2.equals(objectT1));
				assertTrue("Hashcode is not the same for equal instances", objectT1.hashCode() == objectT2.hashCode());
			} else if (objectT2.equals(objectT1))
			{
				fail("Equals is not symetric");
			}
		}
	}

	/**
	 * Tests that equals is transitive i.e. a.equals(b) & b.equals(c) => a.equals(c). This test uses three object iterators. One for a, one for b, and
	 * one for c, where a,b,c are from the above rule. Each of these iterators are instantiated with the same number of elements.
	 */
	@Test
	public void testEqualsTransitive()
	{
		final int iterationsForThisTest = getIterationsForTransitivityTest();
		final Iterator<T> objectTIterator1 = newObjectIterator(iterationsForThisTest);
		final Iterator<T> objectTIterator2 = newObjectIterator(iterationsForThisTest);
		final Iterator<T> objectTIterator3 = newObjectIterator(iterationsForThisTest);

		while (objectTIterator1.hasNext())
		{
			assertTrue("The three object iterators don't have equal number of objects", objectTIterator2.hasNext());
			assertTrue("The three object iterators don't have equal number of objects", objectTIterator3.hasNext());
			final T objectT1 = objectTIterator1.next();
			final T objectT2 = objectTIterator2.next();
			final T objectT3 = objectTIterator3.next();

			if (objectT1.equals(objectT2) && objectT1.equals(objectT3))
			{
				assertTrue("equals is not transitive", objectT2.equals(objectT3));
			}
		}
	}

	/**
	 * Tests that <c>equal</c> returns false if <c>null</c> is passed as argument
	 * 
	 */
	@Test
	public void testEqualsReturnsFalseIfNullPassed()
	{
		final T objectT = this.newEqualInstanceCheckCorrect();
		assertFalse("equals returned true when null passed for other object", objectT.equals(null));
	}

	/**
	 * Tests that <c>equal</c> returns false if a non-equal object is passed as an argument. The two non equal instances are created by calling
	 * <c>newEqualInstance</c> and <c> newNonEqualInstance</c>
	 * 
	 */
	@Test
	public void testEqualsReturnsFalseOnNonEquals()
	{
		final T objectT1 = this.newEqualInstanceCheckCorrect();
		final T objectT2 = this.newNonEqualInstanceCheckCorrect();
		assertTrue("Equals reported true for instances assumed non equal.", !objectT1.equals(objectT2) && !objectT2.equals(objectT1));
	}

	/**
	 * Tests that <c>equal</cL returns true if an equal object is passed as an argument. The instances that are compared are obtained by two
	 * consecutive calls to <c>newEqualInstance</c>
	 * 
	 */
	@Test
	public void testEqualsReturnsTrueOnEquals()
	{
		final T objectT1 = this.newEqualInstanceCheckCorrect();
		final T objectT2 = this.newEqualInstanceCheckCorrect();
		assertTrue("Equals reported false for instances assumed equal.", objectT1.equals(objectT2) && objectT2.equals(objectT1));
	}

	/**
	 * Tests that equals returns false if an object of another class is passed. In case that the tested class is not final this test passes a subclass
	 * of the test class as an argument to <c>equals</c>. The passed ancestor object has the same value for all its field. This is intended to cahtch
	 * wrong implementations using "instanceof" rather than <c>getClass() == other.getClass()</c>
	 * 
	 */
	@Test
	public void testEqualsReturnsFalseIfOtherClassPassed()
	{
		final T objectT = newEqualInstanceCheckCorrect();
		Object object = newAncestorEqualInstanceCheckCorrect();
		if (object == null)
		{
			// class is final. No ancestor could be created. Use some other class.
			object = new Object();
		}
		assertFalse("equals returned true when object of another type passed", objectT.equals(object));
	}

	/**
	 * Tests that <c>equals</c> is properly implemented for use in subclasses. It takes two equal ancestor instances and compares them for equality.
	 * Since the ancestor class does not override the equals method. The tests asserts that equals should return true.
	 * 
	 */
	@Test
	public void testEqualsSuitableForAncestors()
	{
		final T ancestorT1 = newAncestorEqualInstanceCheckCorrect();
		final T ancestorT2 = newAncestorEqualInstanceCheckCorrect();
		if (ancestorT1 == null)
		{
			assertNull(
											"The newAncestorInstance() method returned null on the second invocation, while it return a value differet than null from the first",
											ancestorT2);
			return;
		}
		assertTrue(ancestorT1.equals(ancestorT2) && ancestorT2.equals(ancestorT1));

	}

	private T newAncestorEqualInstanceCheckCorrect()
	{
		final T ancestorT = newAncestorEqualInstance();
		if (ancestorT == null)
		{
			assertTrue("newAncestorEqualInstance returned null, although the test class ", Modifier.isFinal(testClass.getModifiers()));
		} else
		{
			assertTrue("newAncestorEqualInstance returned object of class " + ancestorT.getClass() + ", which is not subtype of "
											+ testClass.getName(), testClass.isAssignableFrom(ancestorT.getClass()));
		}
		return ancestorT;
	}

	private T newEqualInstanceCheckCorrect()
	{
		final T objectT = newEqualInstance();
		assertTrue("newEqualInstance returned object of class " + objectT.getClass().getName() + "Expected was class " + testClass.getName(),
										testClass == objectT.getClass());
		return objectT;
	}

	private T newNonEqualInstanceCheckCorrect()
	{
		final T objectT = newNonEqualInstance();
		assertTrue("newNonEqualInstance returned object of class " + objectT.getClass().getName() + "Expected was class " + testClass.getName(),
										testClass == objectT.getClass());
		return objectT;
	}

}
