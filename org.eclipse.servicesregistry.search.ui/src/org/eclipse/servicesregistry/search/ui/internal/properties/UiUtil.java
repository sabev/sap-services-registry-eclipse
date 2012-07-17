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

import org.eclipse.swt.custom.PaintObjectEvent;
import org.eclipse.swt.custom.PaintObjectListener;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.GlyphMetrics;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;


/**
 * Utility class for creating "fancy" UI
 * 
 * @author Danail Branekov
 */
class UiUtil
{
	private static final String IMAGE_OFFSET = "  "; //$NON-NLS-1$
	/**
	 * Creates a styled text with image and text specified. The created text in the control is selectable. On double click on the text control, only the text is selected (the image remains unselected). Also, selection is lost when the focus leaves the control
	 * 
	 * @param parent
	 *            the parent composite
	 * @param text
	 *            the initial text
	 * @param image
	 *            the image; null if no image is to be displayed
	 * @param style
	 *            the style. Check {@link StyledText} javadoc for supported styles
	 */
	public StyledText createSelectableImageLabel(final Composite parent, final String text, final Image image, final int style)
	{
		final StyledText styledText = createDefaultStyledText(parent, text, image, style);
		installSelectTextDblClickListener(styledText, image == null ? 0 : IMAGE_OFFSET.length());

		return styledText;
	}

	private StyledText createDefaultStyledText(final Composite parent, final String text, final Image image, final int style)
	{
		final StyledText styledText = new StyledText(parent, style);
		setAppropriateText(styledText, text, image);
		installImage(styledText, image);

		intallDeselectTextOnFocusLostListener(styledText);

		return styledText;
	}

	private void setAppropriateText(final StyledText text, final String desiredText, final Image img)
	{
		text.setText((img == null ? "" : IMAGE_OFFSET) + desiredText); //$NON-NLS-1$
	}

	private void intallDeselectTextOnFocusLostListener(final StyledText text)
	{
		text.addFocusListener(new FocusAdapter()
		{
			@Override
			public void focusLost(FocusEvent e)
			{
				text.setSelection(0);
			}
		});
	}

	private void installSelectTextDblClickListener(final StyledText text, final int startIndex)
	{
		text.setDoubleClickEnabled(false);
		text.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseDoubleClick(MouseEvent e)
			{
				text.setSelection(startIndex, text.getContent().getCharCount());
			}
		});
	}

	private void installImage(final StyledText text, final Image image)
	{
		if (image == null)
		{
			return;
		}

		StyleRange style = new StyleRange();
		style.start = 0;
		style.length = 1;
		Rectangle rect = image.getBounds();
		style.metrics = new GlyphMetrics(rect.height, 0, rect.width);
		text.setStyleRange(style);
		text.addPaintObjectListener(new PaintObjectListener()
		{
			public void paintObject(PaintObjectEvent event)
			{
				GC gc = event.gc;
				StyleRange style = event.style;
				int x = event.x;
				int y = event.y + event.ascent - style.metrics.ascent;
				gc.drawImage(image, x, y);
			}
		});
	}
}
