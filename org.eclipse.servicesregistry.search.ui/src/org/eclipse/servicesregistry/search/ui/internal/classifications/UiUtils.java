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
package org.eclipse.servicesregistry.search.ui.internal.classifications;

import java.util.Arrays;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Tree;

/**
 * Utility methods for creating the UIs of this module.
 * 
 * @author Joerg Dehmel
 */
final class UiUtils
{

	private static final Display DISPLAY = Display.getDefault();

	/** color white */
	public static final Color COLOR_WHITE = DISPLAY.getSystemColor(SWT.COLOR_WHITE);

	@SuppressWarnings("unused")
	private static final Color[] colors = new Color[] { DISPLAY.getSystemColor(SWT.COLOR_GREEN), DISPLAY.getSystemColor(SWT.COLOR_RED),
					DISPLAY.getSystemColor(SWT.COLOR_BLUE), DISPLAY.getSystemColor(SWT.COLOR_YELLOW), DISPLAY.getSystemColor(SWT.COLOR_CYAN) };

	private static final String[] CLASSES_TO_COLOR = new String[] { "org.eclipse.swt.widgets.Shell", "org.eclipse.swt.widgets.Composite", //$NON-NLS-1$ //$NON-NLS-2$
					"org.eclipse.ui.forms.widgets.LayoutComposite", "org.eclipse.swt.widgets.Group" }; //$NON-NLS-1$ //$NON-NLS-2$

	@SuppressWarnings("unused")
	private static final List<String> CLASSES_TO_COLOR_LIST = Arrays.asList(CLASSES_TO_COLOR);

	private UiUtils()
	{
		// dc
	}

	/**
	 * Creates a new composite without style.
	 * 
	 * @param parent
	 *            parent composite
	 * @return new composite
	 */
	public static Composite createComposite(final Composite parent)
	{
		assert parent != null;
		return new Composite(parent, SWT.NONE);
	}

	/**
	 * Creates a table with a certain swt style.
	 * 
	 * @param parent
	 *            parent composite
	 * @param style
	 *            swt style
	 * @return new table
	 */
	public static Table createTable(final Composite parent, final int style)
	{
		assert parent != null;
		return new Table(parent, style);
	}

	/**
	 * Creates a tree with a certain swt style.
	 * 
	 * @param parent
	 *            parent composite
	 * @param style
	 *            swt style
	 * @return new tree
	 */
	public static Tree createTree(final Composite parent, int style)
	{
		assert parent != null;
		return new Tree(parent, style);
	}
	
	public static FormData fillingParentFormLayoutData(final int height)
	{
		final FormData fd = fillingParentFormLayoutData();
		fd.height = height;

		return fd;
	}
	
	public static FormData fillingParentFormLayoutData()
	{
		final FormData fd = new FormData();
		fd.top = new FormAttachment(0, 0);
		fd.left = new FormAttachment(0, 0);
		fd.right = new FormAttachment(100, 0);

		return fd;
	}	
}
