/*******************************************************************************
 * Copyright (c) 2013 Luigi Sgro. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Luigi Sgro - initial API and implementation
 ******************************************************************************/
package dao;

import java.util.Date;
/**
 * Capital interface.
 */
public interface ICapital {
	
	double getMainInflow();

	double getMainOutflow();

	double getMainNetflow();

	double getMainRate();

	double getRetailInflow();

	double getRetailOutflow();

	double getRetailNetflow();

	double getRetailRate();
	
	double getVolumn();

	double getPrice();

	double getTurnoverRate();

	double getPER();

	double getMarketValue();

	double getTotalValue();

	 double getPBR();
	/**
	 * Timestamp of the last update
	 * This information is useful when the period has not expired yet, or when the bar has been stored while incomplete
	 */
	Date getLastUpdate();
}
