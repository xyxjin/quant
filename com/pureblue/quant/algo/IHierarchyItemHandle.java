/*******************************************************************************
 * Copyright (c) 2013 Luigi Sgro. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Luigi Sgro - initial API and implementation
 ******************************************************************************/
package com.pureblue.quant.algo;

import com.pureblue.quant.model.IPersistentIdentifiable;
import com.pureblue.quant.model.IPrettyNamed;

/**
 * Handle to an item of the algorithm hierarchy.
 * @see ITradingHierarchyManager
 */
public interface IHierarchyItemHandle extends IPrettyNamed, IPersistentIdentifiable {
}
