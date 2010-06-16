/*******************************************************************************
 * Copyright (c) 2004, 2005 Elias Volanakis and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Elias Volanakis - initial API and implementation
 *******************************************************************************/
package org.integratedmodelling.thinkscape.gedit.parts;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

import org.integratedmodelling.thinkscape.gedit.model.ModelShape;
import org.integratedmodelling.thinkscape.gedit.model.ModelDiagram;


/**
 * Factory that maps model elements to TreeEditParts.
 * TreeEditParts are used in the outline view of the ShapesEditor.
 * @author Elias Volanakis
 */
public class ShapesTreeEditPartFactory implements EditPartFactory {

/* (non-Javadoc)
 * @see org.eclipse.gef.EditPartFactory#createEditPart(org.eclipse.gef.EditPart, java.lang.Object)
 */
public EditPart createEditPart(EditPart context, Object model) {
	if (model instanceof ModelShape) {
		return new ShapeTreeEditPart((ModelShape) model);
	}
	if (model instanceof ModelDiagram) {
		return new DiagramTreeEditPart((ModelDiagram) model);
	}
	return null; // will not show an entry for the corresponding model instance
}

}
