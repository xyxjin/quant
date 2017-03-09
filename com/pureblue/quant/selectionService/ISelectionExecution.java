package com.pureblue.quant.selectionService;

import java.util.Date;
import java.util.Map;

import com.pureblue.quant.algo.IManagedRunnable;
import com.pureblue.quant.model.ISeries;
import com.pureblue.quant.model.ISeriesPoint;
import com.pureblue.quant.model.ISeriesProcessor;

public interface ISelectionExecution extends ISeriesProcessor<Date, Double>, IManagedRunnable {
    /**
     * Returns the input series of the execution 
     */
    Map<String, ? extends ISeries<Date, Double, ? extends ISeriesPoint<Date, Double>>> getInput();
    /**
     * Returns the output series of the execution
     */
    ISeries<Date, Double, ISeriesPoint<Date, Double>> getOutput();
}