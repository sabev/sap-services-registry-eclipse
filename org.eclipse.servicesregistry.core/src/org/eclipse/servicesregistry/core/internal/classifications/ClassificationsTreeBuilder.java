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
package org.eclipse.servicesregistry.core.internal.classifications;

import org.eclipse.servicesregistry.core.classifications.IClassificationSystemNode;
import org.eclipse.servicesregistry.core.classifications.IHierarchicalClassificationValueNode;
import org.eclipse.servicesregistry.core.classifications.ITreeNodeList;
import org.eclipse.servicesregistry.core.internal.classifications.finders.IClassificationSystemsFinder;
import org.eclipse.servicesregistry.core.internal.classifications.finders.IClassificationValuesFinder;
import org.eclipse.servicesregistry.core.internal.classifications.nodes.ClassificationSystemNode;
import org.eclipse.servicesregistry.core.internal.classifications.nodes.ClassificationValueNode;
import org.eclipse.servicesregistry.core.internal.classifications.nodes.HierarchicalValueNode;
import org.eclipse.servicesregistry.core.internal.classifications.nodes.TreeNodeList;
import org.eclipse.servicesregistry.proxy.types.ClassificationSystem;
import org.eclipse.servicesregistry.proxy.types.ClassificationSystemValue;
import org.eclipse.servicesregistry.proxy.types.GetClassificationSystemValuesFault;
import org.eclipse.servicesregistry.proxy.types.GetClassificationSystemsGreaterThanVersionFault;
import org.eclipse.servicesregistry.proxy.types.ServicesRegistrySigetClassificationSystemValuesFault;
import org.eclipse.servicesregistry.proxy.types.ServicesRegistrySigetClassificationSystemsGreaterThanVersionFault;

public class ClassificationsTreeBuilder implements IClassificationsTreeBuilder
{
	private final IClassificationSystemsFinder systemsFinder;
	private final IClassificationValuesFinder valuesFinder;

	public ClassificationsTreeBuilder(final IClassificationSystemsFinder systemsFinder, final IClassificationValuesFinder valuesFinder)
	{
		this.systemsFinder = systemsFinder;
		this.valuesFinder = valuesFinder;
	}

	@Override
	public ITreeNodeList buildTree() throws GetClassificationSystemsGreaterThanVersionFault, ServicesRegistrySigetClassificationSystemsGreaterThanVersionFault, GetClassificationSystemValuesFault, ServicesRegistrySigetClassificationSystemValuesFault
	{

		final ITreeNodeList result = new TreeNodeList();

		for (ClassificationSystem classifSystem : systemsFinder.findClassificationSystems())
		{
			final IClassificationSystemNode classifSystemNode = new ClassificationSystemNode(classifSystem);
			result.getRootNodes().add(classifSystemNode);
			processClassificationSystem(classifSystem, classifSystemNode);
		}

		return result;
	}

	private void processClassificationSystem(final ClassificationSystem classifSystem, final IClassificationSystemNode parentSystem) throws GetClassificationSystemValuesFault, ServicesRegistrySigetClassificationSystemValuesFault
	{
		for (ClassificationSystemValue value : valuesFinder.findValues(classifSystem))
		{
			createClassificationSystemValue(value, parentSystem);
		}
	}

	private void createClassificationSystemValue(final ClassificationSystemValue value, final IClassificationSystemNode parentSystem)
	{
		if (parentSystem.getType() == ClassificationSystemType.FLAT)
		{
			createSimpleValueNode(value, parentSystem);
		}
		else
		{
			createHierarchicalClassificationValue(value, parentSystem, null);
		}
	}

	private void createHierarchicalClassificationValue(final ClassificationSystemValue value, final IClassificationSystemNode system, final IHierarchicalClassificationValueNode parent)
	{
		final IHierarchicalClassificationValueNode hierarchicalValueNode = new HierarchicalValueNode(value, system, parent);
		for (ClassificationSystemValue childValue : value.getChildren())
		{
			createHierarchicalClassificationValue(childValue, system, hierarchicalValueNode);
		}
	}

	private void createSimpleValueNode(final ClassificationSystemValue value, final IClassificationSystemNode parentSystem)
	{
		new ClassificationValueNode(value, parentSystem, parentSystem);
	}
}
