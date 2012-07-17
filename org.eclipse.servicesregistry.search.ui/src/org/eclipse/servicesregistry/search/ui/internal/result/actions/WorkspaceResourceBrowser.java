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
package org.eclipse.servicesregistry.search.ui.internal.result.actions;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.window.Window;
import org.eclipse.servicesregistry.search.ui.internal.text.SearchUIMessages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;

/**
 * This class provides a dialog for browsing workspace resources.
 */

public class WorkspaceResourceBrowser extends Dialog {
	private IWorkspaceRoot input;

	private IResource selectedResource;

	private TreeViewer viewer;

	/**
	 * Constructs a new <code>DialogResourceBrowser</code> under the given
	 * <code>parent Shell</code>. The dialog renders all workspace resources.
	 * 
	 * @param shell
	 *            the shell the browser will be created on
	 */
	public WorkspaceResourceBrowser(final Shell shell) {
		super(shell);

		input = ResourcesPlugin.getWorkspace().getRoot();
		setShellStyle(SWT.DIALOG_TRIM | SWT.RESIZE | SWT.APPLICATION_MODAL);
	}

	/**
	 * Returns the selections made in the dialog if OK was pressed, or null if
	 * Cancel was pressed. Returns null if the dialog has never been opened.
	 * 
	 * @return An array of selected resources, possibly empty, possibly null.
	 */
	public final IResource getSelection() {
		return selectedResource;
	}

	/**
	 * Sets the selection of the resource tree viewer to the specified resource.
	 * 
	 * @param pSelectedResource
	 *            the resource to be selected
	 */
	public final void setSelection(final IResource pSelectedResource) {
		selectedResource = pSelectedResource;
	}

	@Override
	protected final void cancelPressed() {
		setReturnCode(Window.CANCEL);
		super.cancelPressed();
	}

	@Override
	protected final void okPressed() {
		final IStructuredSelection structuredSelection = (IStructuredSelection) viewer
				.getSelection();
		final Object element = structuredSelection.getFirstElement();
		selectedResource = (IResource) element;
		setReturnCode(Window.OK);
		super.okPressed();
	}

	@Override
	protected final void configureShell(final Shell shell) {
		super.configureShell(shell);
		shell.setText(SearchUIMessages.WorkspaceResourceBrowser_BROWSER_SHELL_TITLE);

	}

	@Override
	protected Control createDialogArea(final Composite parent) {
		final Composite dialogPane = (Composite) super.createDialogArea(parent);
		final GridData dialogPaneGd = new GridData(
				GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL
						| GridData.VERTICAL_ALIGN_FILL | GridData.GRAB_VERTICAL);
		dialogPaneGd.widthHint = 400;
		dialogPaneGd.heightHint = 300;
		dialogPaneGd.grabExcessVerticalSpace = true;
		dialogPaneGd.grabExcessHorizontalSpace = true;
		dialogPane.setLayoutData(dialogPaneGd);

		final WorkspaceBrowserTreeViewerFactory treeVFactory = new WorkspaceBrowserTreeViewerFactory();
		viewer = treeVFactory.createWorkspaceTreeViewer(dialogPane);
		final GridData treeGd = new GridData(GridData.HORIZONTAL_ALIGN_FILL
				| GridData.GRAB_HORIZONTAL | GridData.VERTICAL_ALIGN_FILL
				| GridData.GRAB_VERTICAL);
		treeGd.grabExcessVerticalSpace = true;
		treeGd.grabExcessHorizontalSpace = true;
		viewer.getTree().setLayoutData(treeGd);
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(final SelectionChangedEvent event) {
				final IStructuredSelection selection = (IStructuredSelection) event
						.getSelection();
				final Object element = selection.getFirstElement();
				if (element != null) {
					computeButtonStatus(true);
				}
			}
		});

		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(final DoubleClickEvent event) {
				final IStructuredSelection selection = (IStructuredSelection) event
						.getSelection();
				final Object element = selection.getFirstElement();
				if (element != null) {
					okPressed();
				}
			}
		});
		viewer.setInput(input);
		if (selectedResource != null) {
			viewer.setSelection(new TreeSelection(new TreePath(
					new Object[] { selectedResource })));
		}
		return dialogPane;
	}
	
	protected ViewerFilter createBrowseViewerFilter()	{
		return new ViewerDestinationFilter();
		
	}
	
	private class ViewerDestinationFilter extends ViewerFilter {
		@Override
		public boolean select(final Viewer pViewer,
				final Object parentObject, final Object object) {
			return (((IResource) object).getType() != IResource.FILE)
					&& isImportDestinationAllowed((IResource) object);
		}
		/**
		 * Utility method that returns if the destination element is allowed for the
		 * import of the WSDL. <br>
		 * The destination element is not allowed as a destination for the import of
		 * the WSDL if:
		 * <ul>
		 * <li>the element is derived</li>
		 * <li>the element is team private member</li>
		 * </ul>
		 * 
		 * @param element -
		 *            the element to be checked
		 * @return whether the destination element is allowed for destination
		 */
		private boolean isImportDestinationAllowed(IResource element) {
			return !(element.isDerived() || element.isTeamPrivateMember());
		}
	}

	

	/**
	 * Toggles the state of the ok button.
	 * 
	 * @param state
	 *            for true the button will be enabled, otherwise disabled
	 */
	final void computeButtonStatus(final boolean state) {
		final Button okButton = getButton(IDialogConstants.OK_ID);
		if (okButton != null) {
			okButton.setEnabled(state);
		}
	}

	private class WorkspaceProjectFilter extends ViewerFilter {
		@Override
		public boolean select(Viewer viewer, Object parentElement,
				Object element) {
			if (element instanceof IProject) {
				final IProject project = (IProject) element;
				if (project.isAccessible()) {
					return true;
				} else {
					return false;
				}
			}
			return true;
		}
	}

	private class DecoratorsAwareLabelProvider extends WorkbenchLabelProvider {
		@Override
		protected String decorateText(String pInput, Object element) {
			if(element instanceof IProject){
				IProject project = (IProject) element;
				return PlatformUI.getWorkbench().getDecoratorManager().getLabelDecorator().decorateText(project.getName(), project);
			}
			return pInput;
		}
	}
	
	protected TreeViewer viewer() {
		return viewer;
	}
	
	public class WorkspaceBrowserTreeViewerFactory
	{
		public TreeViewer createWorkspaceTreeViewer(final Composite parent)
		{
			final Tree tree = new Tree(parent, SWT.SINGLE | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
			tree.setToolTipText(SearchUIMessages.WorkspaceResourceBrowser_RESUORCE_TREE_TOOLTIP);

			final TreeViewer viewer = new TreeViewer(tree);
			viewer.setContentProvider(new WorkbenchContentProvider());
			viewer.setFilters(new ViewerFilter[] { new WorkspaceProjectFilter() });

			viewer.setLabelProvider(new DecoratorsAwareLabelProvider());
			viewer.addFilter(createBrowseViewerFilter());

			return viewer;
		}
	}
}
