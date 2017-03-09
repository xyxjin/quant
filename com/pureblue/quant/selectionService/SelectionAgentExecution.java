package com.pureblue.quant.selectionService;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.pureblue.quant.algo.IInputSeriesStreamer;
import com.pureblue.quant.algo.InputSeriesStreamer;
import com.pureblue.quant.model.IMutableSeries;
import com.pureblue.quant.model.ISeries;
import com.pureblue.quant.model.ISeriesAugmentable;
import com.pureblue.quant.model.ISeriesPoint;

public class SelectionAgentExecution implements ISelectionExecution {
    private Logger logger;
    private static final long ALGO_START_WAIT_QUANTUM = 100;
    protected volatile ISelectionAgent selectionAgent;
    protected volatile Map<String, ? extends ISeries<Date, Double, ? extends ISeriesPoint<Date, Double>>> inputSeriesMap;
    protected volatile ISeriesAugmentable<Date, Double, ISeriesPoint<Date, Double>> outputSeries;
    volatile IInputSeriesStreamer inputSeriesStreamer;
    
    public SelectionAgentExecution(ISelectionAgent selectionAgent) {
        this.logger = Logger.getLogger(getClass());
        logger.info("SelectionAgentExecution start!!!");
        
        this.selectionAgent = selectionAgent;
    }

    @Override
    public void wire(Map<String, ? extends ISeries<Date, Double, ? extends ISeriesPoint<Date, Double>>> input, ISeriesAugmentable<Date, Double, ISeriesPoint<Date, Double>> output) {
        this.inputSeriesMap = inputSeriesMap;
        this.outputSeries = output;
    }

    @Override
    public void unwire() {
        this.inputSeriesMap = null;
        this.outputSeries = null;
    }

    @Override
    public void run() {
        Map<String, IMutableSeries<Date, Double, ? extends ISeriesPoint<Date, Double>>> targetSeriesMap = new HashMap<String, IMutableSeries<Date, Double, ? extends ISeriesPoint<Date, Double>>>();
        for (Map.Entry<String, ? extends ISeries<Date, Double, ? extends ISeriesPoint<Date, Double>>> entry : inputSeriesMap.entrySet()) {
            targetSeriesMap.put(entry.getKey(), entry.getValue().createEmptyMutableSeries(entry.getValue().getPersistentID()));
        }
        this.inputSeriesStreamer = new InputSeriesStreamer(inputSeriesMap, targetSeriesMap);
        Map<String, ? extends ISeries<Date, Double, ? extends ISeriesPoint<Date, Double>>> simulatedInputSeries = inputSeriesStreamer.getStreamingSeries();
        selectionAgent.wire(simulatedInputSeries, outputSeries);
        Thread algoThread = new Thread(new Runnable() {
            public void run() {
                selectionAgent.run();
            }
        });
        algoThread.start();
        while (selectionAgent.getRunningStatus() == RunningStatus.NEW) {
            try {
                Thread.sleep(ALGO_START_WAIT_QUANTUM);
            } catch (InterruptedException e) {
                logger.warn("Interrupted while waiting for algo to start", e);
            }
        }
        inputSeriesStreamer.run();
        selectionAgent.inputComplete();
        try {
            algoThread.join();
        } catch (InterruptedException e) {
            logger.fatal("Algo thread interrupted", e);
        }
        selectionAgent.unwire();
    }

    @Override
    public void pause() {
        selectionAgent.pause();
    }

    @Override
    public void resume() {
        selectionAgent.resume();
    }

    @Override
    public void kill() {
        selectionAgent.kill();
    }

    @Override
    public RunningStatus getRunningStatus() {
        return selectionAgent.getRunningStatus();
    }

    @Override
    public Map<String, ? extends ISeries<Date, Double, ? extends ISeriesPoint<Date, Double>>> getInput() {
        return inputSeriesMap;
    }

    @Override
    public ISeries<Date, Double, ISeriesPoint<Date, Double>> getOutput() {
        return outputSeries;
    }

}
