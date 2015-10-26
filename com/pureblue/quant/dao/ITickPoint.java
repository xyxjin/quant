/*******************************************************************************

 * Copyright (c) 2013 Luigi Sgro. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Luigi Sgro - initial API and implementation
 ******************************************************************************/
package com.pureblue.quant.dao;

import java.util.Date;

import com.pureblue.quant.model.DataType;
import com.pureblue.quant.model.ISeriesPoint;

/**
 * Tick data point
 */
public interface ITickPoint extends ISeriesPoint<Date, Double> {
	DataType getDataType();
	Integer getSize();
}
