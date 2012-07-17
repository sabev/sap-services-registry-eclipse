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
package org.eclipse.servicesregistry.search.ui.internal.properties;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.events.IHyperlinkListener;
import org.eclipse.ui.forms.widgets.ImageHyperlink;
import org.eclipse.ui.menus.CommandContributionItem;
import org.eclipse.ui.menus.CommandContributionItemParameter;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;

public abstract class GenericPropertiesSection<T> extends AbstractPropertySection
{
	private T input;
	private Composite propertiesComposite;
	private Composite propertiesParent;
	private TabbedPropertySheetWidgetFactory widgetFactory;
	private static final String copyMenuTitle = "Copy {0}";
	
	private final IHyperlinkListener followLinkListener = new IHyperlinkListener()
	{    
		
		public void linkActivated(HyperlinkEvent e) {
			try {
				
				openUrlInBrowserSupprt(new URL(e.getHref().toString()));
			} catch (MalformedURLException e2) {
				//should not happen. Url has been constructed by us.
				throw new RuntimeException(e2);
			}
		}

		public void linkEntered(HyperlinkEvent e) {
	
		}

		public void linkExited(HyperlinkEvent e) {
		
		}
	};

	protected abstract void recreateProperties();
	
	protected void openUrlInBrowserSupprt(URL url)
	{
		try {
			browserSupport().createBrowser(java.util.UUID.randomUUID().toString()).openURL(url);
		} catch (PartInitException e) {
			throw new RuntimeException(e);
		}
	}
	
	private IWorkbenchBrowserSupport browserSupport() {
		return PlatformUI.getWorkbench().getBrowserSupport();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void setInput(IWorkbenchPart part, ISelection selection) {
		super.setInput(part, selection);
		input = (T)((IStructuredSelection)selection).getFirstElement();
		refresh();
	}
	
	@Override
	public final void  refresh() {
		clearProperties();
		recreateProperties();
		if(propertiesComposite != null && !propertiesComposite.isDisposed())		{
			propertiesComposite.layout(true, true);
			propertiesComposite.setBounds(0,0, propertiesParent.getBounds().width,propertiesParent.getBounds().height);
		}
	}

	protected void clearProperties()
	{
		if(propertiesComposite == null || propertiesComposite.isDisposed())		{
			return;
		}
		for (Control child: propertiesComposite.getChildren())		{
			child.dispose();
		}
	}
	
	protected T getInput()
	{
		return input;
	}


	@Override
	public void createControls(Composite parent, TabbedPropertySheetPage aTabbedPropertySheetPage)
	{
		super.createControls(parent, aTabbedPropertySheetPage);
		widgetFactory = aTabbedPropertySheetPage.getWidgetFactory();
		propertiesParent = parent;
		propertiesComposite = createPropertiesComposite();
		propertiesComposite.setVisible(true);
	}

	protected void createProperty(final Composite parent, final TabbedPropertySheetWidgetFactory widgetFactory, final String propName, final String propValue)	{
		final Label propLabel = widgetFactory.createLabel(parent, propName);
		propLabel.setLayoutData(gridDataForPropertyControl(false));

		final Text propText = widgetFactory.createText(parent, propValue, SWT.READ_ONLY);
		propText.setLayoutData(gridDataForPropertyControl(true));
	}

	private GridData gridDataForPropertyControl(final boolean grabExcessHorizontalSpace)	{
		return new GridData(grabExcessHorizontalSpace ? SWT.FILL : SWT.BEGINNING, SWT.BEGINNING, grabExcessHorizontalSpace, false, 1, 1);
	}

	private Composite createPropertiesComposite()	{
		final Composite c = widgetFactory.createComposite(propertiesParent);
		c.setLayout(new GridLayout(2, false));
		return c;
	}

	
	protected Control createTextProperty(String name, String value, final String objectDisplayName) {
		CLabel label = getWidgetFactory().createCLabel(propertiesComposite, name + ":"); //$NON-NLS-1$
		final StyledText t = (new UiUtil()).createSelectableImageLabel(propertiesComposite, value, null, SWT.READ_ONLY);
		configureTextProperty(t, value, objectDisplayName);
		putPropertyInTable(label, t);
		return t;
	}
	
	private ImageHyperlink createSimpleLinkProperty(String title, String link, boolean addSemicolon)	{
		final String titleMod = addSemicolon ? title + ":" : title;//$NON-NLS-1$
		final CLabel label = getWidgetFactory().createCLabel(propertiesComposite, titleMod); 
		final ImageHyperlink l =  getWidgetFactory().createImageHyperlink(propertiesComposite, SWT.NONE);
		configureSimpleLinkProperty(l, link);
		putPropertyInTable(label, l);
		return l;
	}
	
	private void configureSimpleLinkProperty(final ImageHyperlink link, final String href)	{
		link.setText(href);
		link.setHref(href);
		link.setToolTipText(""); //$NON-NLS-1$
		link.addHyperlinkListener(followLinkListener);
	}
	
	private void configureTextProperty(final Control textControl, final String text, final String objectDisplayName)	{
		final MenuManager mm = createMenuManager(textControl);
		mm.add(new CommandContributionItem(new CommandContributionItemParameter(PlatformUI.getWorkbench(), null, "org.eclipse.ui.edit.copy", //$NON-NLS-1$
										CommandContributionItem.STYLE_PUSH)));
		createDefaultTextPropertyMenu(mm, textControl, text, objectDisplayName);

		applyMenuToControl(mm, textControl);
		textControl.setToolTipText(createDefaultTextPropertyTooltip(text, objectDisplayName));
	}
	
	protected Control createLinkProperty(String title, String link) {
		final Control l = createSimpleLinkProperty(title, link, true);
		configureLinkProperty(l, link);
		return l;
	}
	
	protected void configureLinkProperty(final Control link, final String href)	{
		final MenuManager mm = createMenuManager(link);
		createDefaultTextPropertyMenu(mm, link, href, "URL");
		applyMenuToControl(mm, link);
		link.setToolTipText(createDefaultTextPropertyTooltip(href, "URL")); 
	}
	
	protected String createDefaultTextPropertyTooltip(final String text, final String objectDisplayName)	{
		return text + "\n" + copyHint(objectDisplayName);//$NON-NLS-1$
	}
	
	private String copyHint(final String objectDisplayName)	{
		return MessageFormat.format("To copy value select the ''{0}'' context menu.", MessageFormat.format(copyMenuTitle, objectDisplayName)); 
	}
	
	protected void createDefaultTextPropertyMenu(final MenuManager mm, Control c, String text, final String objectDisplayName)	{
		mm.add(newCopyTextContentAction(c, text, objectDisplayName));
	}
	
	private Action newCopyTextContentAction(final Control textControl, final String content, final String objectDisplayName)	{
		return new Action() {
			@Override
			public String getText() {
				return MessageFormat.format(copyMenuTitle, objectDisplayName);
			}
			@Override
			public void run() {
				final TextTransfer textTransfer = TextTransfer.getInstance();
				final Clipboard cb = new Clipboard(textControl.getDisplay());
				cb.setContents(new Object[] {content},
		            new Transfer[] { textTransfer });
		   }
		};
	}
	
	protected void applyMenuToControl(MenuManager mm, Control c)	{
		Menu m = mm.createContextMenu(c);
		c.setMenu(m);
	}
	
	private void putPropertyInTable(CLabel propLabel, Control propControl)
	{
		final int length = propLabel.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
		final int oldLength;
		GridData gridData;
		if (propertiesComposite.getChildren().length > 2)
		{
			gridData = (GridData)propertiesComposite.getChildren()[0].getLayoutData();
			oldLength = gridData.widthHint;
		} else {
			oldLength = -1;
			gridData = null;
		}
		if (length > oldLength)
		{
			gridData = new GridData(SWT.BEGINNING, SWT.FILL, false, true);
			gridData.widthHint = length;
			for(int i = 0; i < propertiesComposite.getChildren().length - 1; i += 2)
			{
				propertiesComposite.getChildren()[i].setLayoutData(gridData);
			}
		} else
		{
			propLabel.setLayoutData(gridData);
		}
		
		final GridData valueData = new GridData(SWT.BEGINNING, SWT.FILL, true, true);
		valueData.horizontalIndent = 5;
		propControl.setLayoutData(valueData);
	}
	
	protected MenuManager createMenuManager(final Control control)	{
		final MenuManager mm = new MenuManager();
		return mm;
	}
	
}
