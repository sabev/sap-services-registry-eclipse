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

import java.util.Collections;
import java.util.List;

import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.platform.discovery.util.internal.ContractChecker;
import org.eclipse.servicesregistry.core.classifications.IClassificationSystemNode;
import org.eclipse.servicesregistry.core.classifications.IClassificationValueNode;
import org.eclipse.servicesregistry.core.classifications.ITreeNode;
import org.eclipse.servicesregistry.core.classifications.ITreeNodeList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * Factory class for creating the services registry classification tree
 */
public class SrClassificationTreeFactory
{
	private Composite dataPane;
	private Tree dataTree;
	private CheckboxTreeViewer dataViewer;
	private ITreeNodeList classificationValues;

	/**
	 * Creates the tree. It is expected that the parent composite has {@link GridLayout}
	 * 
	 * @param parent
	 *            the parent composite
	 * @return {@link IServicesRegistryTree} instance which provides access to the UI components
	 */
	public IServicesRegistryTree createServicesRegistryTree(final Composite parent)
	{
		ContractChecker.nullCheckParam(parent, "parent"); //$NON-NLS-1$

		this.dataPane = UiUtils.createComposite(parent);
		this.dataPane.setLayout(new FormLayout());

		this.dataTree = UiUtils.createTree(dataPane, SWT.BORDER | SWT.MULTI);
		this.dataTree.setLayoutData(UiUtils.fillingParentFormLayoutData(dataTree.computeSize(SWT.DEFAULT, SWT.DEFAULT).y));
		this.dataViewer = new CheckboxTreeViewer(this.dataTree);
		this.dataViewer.setAutoExpandLevel(1);
		this.dataViewer.setComparator(new ViewerComparator());
		addDataTreeLabelProvider();
		addDataTreeContentProvider();
		addDataTreeMouseListener();
		addDataTreeKeyListener();

		return createSrTreeAccessor();
	}
	
	private void update(final ITreeNodeList classificationValues)
	{
		ContractChecker.nullCheckParam(classificationValues, "classificationValues"); //$NON-NLS-1$
		SrClassificationTreeFactory.this.classificationValues = classificationValues;

		SrClassificationTreeFactory.this.dataViewer.setInput(SrClassificationTreeFactory.this.classificationValues);
		SrClassificationTreeFactory.this.dataViewer.refresh();
		SrClassificationTreeFactory.this.dataPane.layout();
	}

	private IServicesRegistryTree createSrTreeAccessor()
	{
		return new IServicesRegistryTree()
		{
			@Override
			public void update(final ITreeNodeList classificationValues)
			{
				SrClassificationTreeFactory.this.update(classificationValues);
			}

			@Override
			public List<IClassificationValueNode> getSelectedClassificationValues()
			{
				if (SrClassificationTreeFactory.this.classificationValues == null)
				{
					return Collections.emptyList();
				}

				return SrClassificationTreeFactory.this.classificationValues.getSelectedNodes();
			}

			@Override
			public void setReadOnly(boolean readOnly)
			{
				dataViewer.getControl().setEnabled(!readOnly);
			}
		};
	}

	private void addDataTreeLabelProvider()
	{
		this.dataViewer.setLabelProvider(new ClassificationsTreeLabelProvider());
	}

	private void addDataTreeContentProvider()
	{
		this.dataViewer.setContentProvider(new ClassificationsTreeContentProvider());
	}

	private void addDataTreeKeyListener()
	{
		this.dataTree.addKeyListener(new KeyListener()
		{

			public final void keyPressed(final KeyEvent e)
			{
				if (e.character != ' ')
				{
					return;
				}
				handleDataTreeSelectionChanged();
			}

			public final void keyReleased(final KeyEvent e)
			{
				// nothing to be done here
			}

		});
	}

	private void addDataTreeMouseListener()
	{
		this.dataTree.addMouseListener(new MouseListener()
		{
			public final void mouseDown(final MouseEvent e)
			{
				if (e.button != 1 || !isTreeItemImageClicked(e))
				{
					return;
				}
				handleDataTreeSelectionChanged();
			}

			private boolean isTreeItemImageClicked(final MouseEvent e)
			{
				final Point clickPoint = new Point(e.x, e.y);
				final TreeItem item = SrClassificationTreeFactory.this.dataTree.getItem(clickPoint);
				if (item == null)
				{
					return false;
				}
				final Rectangle imagePosition = item.getImageBounds(0);

				// check if clickPoint was inside of imagePosition
				return clickPoint.x >= imagePosition.x && clickPoint.x <= (imagePosition.x + imagePosition.width) && clickPoint.y >= imagePosition.y && clickPoint.y <= (imagePosition.y + imagePosition.height);
			}

			public final void mouseDoubleClick(final MouseEvent e)
			{
				// nothing to be done here
			}

			public final void mouseUp(final MouseEvent e)
			{
				// nothing to be done here
			}
		});
	}

	private void handleDataTreeSelectionChanged()
	{
		ContractChecker.nullCheckField(this.dataViewer, "dataViewer");//$NON-NLS-1$
		final IStructuredSelection sel = (IStructuredSelection) this.dataViewer.getSelection();
		final ITreeNode node = (ITreeNode) sel.getFirstElement();
		if (node == null)
		{
			return;
		}

		if (node instanceof IClassificationSystemNode)
		{
			// classification systems cannot be selected
			return;
		}

		if (node instanceof IClassificationValueNode)
		{
			final IClassificationValueNode valueNode = (IClassificationValueNode) node;
			valueNode.setSelected(!valueNode.isSelected());
		}

		this.dataViewer.refresh();
	}

	public interface IServicesRegistryTree
	{
		/**
		 * Updates the services registry tree with the classification values specified
		 * 
		 * @param ClassificationValues
		 */
		public void update(final ITreeNodeList classificationValues);

		/**
		 * Gets the currently selected classification values
		 * 
		 * @return selected classification values
		 */
		public List<IClassificationValueNode> getSelectedClassificationValues();

		/**
		 * Sets the classification values tree into readonly mode. Being in this mode interaction with from the user is not possible
		 * 
		 * @param readOnly
		 *            true to set the tree as read-only; false otherwise
		 */
		public void setReadOnly(boolean readOnly);
	}
}
