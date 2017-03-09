package com.pureblue.quant.selectionService;

import java.util.Date;
import java.util.List;

import com.pureblue.quant.algo.IManagedRunnable;
import com.pureblue.quant.dao.IOHLCPoint;
import com.pureblue.quant.model.ISeriesProcessor;

public interface ISelectionAgent extends ISeriesProcessor<Date, Double>, IManagedRunnable {

	public boolean evaluate();
	public void wire(List<IOHLCPoint> inputSeries);
	void inputComplete();
}
