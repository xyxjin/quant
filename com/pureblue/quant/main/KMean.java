package com.pureblue.quant.main;

import static java.lang.Math.abs;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.pureblue.quant.ConnectionPool.ConnectionManager;
import com.pureblue.quant.dao.IOHLCPoint;
import com.pureblue.quant.dao.OHLCPointDao;
import com.pureblue.quant.model.TimePeriod;

public class KMean {
    private ArrayList<Double> dataItems;
    private ArrayList<Double> cz;
    private ArrayList<Double> oldCz;
    private ArrayList<Double> row;
    private ArrayList<ArrayList<Double>> groups;

    public static void main(String[] args) {

        ArrayList<Double> srs;
        srs = new ArrayList<Double>();

        ConnectionManager cm = ConnectionManager.getInstance();
        Connection connection = cm.getConnection("tencentstockquotes");
        OHLCPointDao dao = new OHLCPointDao(connection);
        List<IOHLCPoint> data = null;
        try {
            data = dao.findTicker("600030", TimePeriod.formatStringToDate("2017-10-18"), TimePeriod.formatStringToDate("2017-1-1"));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println(data.toString());
        ArrayList<Double> lowsarraylist = new ArrayList<Double>();
        ArrayList<Double> highsarraylist = new ArrayList<Double>();

        for(int i=0;i<data.size();i++) {
            lowsarraylist.add(data.get(i).getLow());
            highsarraylist.add(data.get(i).getHigh());
        }
        try {
            cm.closeConnection("tencentstockquotes", connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Double[] lows = new Double[lowsarraylist.size()];
        lows = lowsarraylist.toArray(lows);
        /* Compare data point to 2 points to the left and 2 to the right
         * if it is lower than the 4, it is considered a minima */
        for (int i = 2; i < (lows.length - 2); i++) {
            if (lows[i] < lows[i-1] && lows[i] < lows[i-2] &&
                    lows[i] < lows[i+1] && lows[i] < lows[i+2]) {
                srs.add(lows[i]);
            }
        }


        // Find maximas
        Double[] highs = new Double[highsarraylist.size()];
        highs = highsarraylist.toArray(highs);

        /* Compare data point to 2 points to the left and 2 to the right
         * if it is high than the 4, it is considered a maxima */
        for (int i = 2; i < (highs.length - 2); i++) {
            if (highs[i] < highs[i-1] && highs[i] < highs[i-2] &&
                    highs[i] < highs[i+1] && highs[i] < highs[i+2]) {
                srs.add(highs[i]);
            }
        }

        /* Filter out irrelevant past data by removing any pivots that are
         * more than 1.5x the latest closing price or lower than half */
        double latestclose = 18.05;
        for (int i = 0; i < srs.size(); i++) {
            if (srs.get(i) > 1.5 * latestclose ||
                    srs.get(i) < latestclose / 2) {
                srs.remove(i);
                i--;
            }
        }

        System.out.println(srs.toString());

        KMean kmean = new KMean(8, srs);
        kmean.print();

    }

    /* KMean takes the pivots and group them into K clustered points.
     * For fairly accurate support/resistance lines, I've decided to use
     * K = 8; therefore there will be 8 S/R lines calculated.
     * Each S/R line will also have a 'strength' value.
     * The stronger the line, the harder it is to be broken through. */

    public KMean(int k, ArrayList<Double> srs) {
        dataItems = new ArrayList<>();
        cz = new ArrayList<>();
        oldCz = new ArrayList<>();
        row = new ArrayList<>();
        groups = new ArrayList<>();

        for (int i = 0; i < k; i++) {
            groups.add(new ArrayList<>());
        }

        for (int i = 0; i < srs.size(); i++) {
            dataItems.add(srs.get(i));
            if (i < k) {
                cz.add(dataItems.get(i));
            }
        }
        int iter = 1;
        do {
            for (double aItem : dataItems) {
                for (double c : cz) {
                    row.add(abs(c - aItem));
                }
                groups.get(row.indexOf(Collections.min(row))).add(aItem);
                row.removeAll(row);
            }
            for (int i = 0; i < k; i++) {
                if (iter == 1) {
                    oldCz.add(cz.get(i));
                } else {
                    oldCz.set(i, cz.get(i));
                }
                if (!groups.get(i).isEmpty()) {
                    cz.set(i, average(groups.get(i)));
                }
            }
            if (!cz.equals(oldCz)) {
                for (int i = 0; i < groups.size(); i++) {
                    groups.get(i).removeAll(groups.get(i));
                }
            }
            iter++;
        } while (!cz.equals(oldCz));
    }

    public double average(ArrayList<Double> list) {
        double sum = 0;
        for (Double value : list) {
            sum = sum + value;
        }
        return sum / list.size();
    }

    // print method
    public void print() {
        String line = "<html>";
//        String line = "";
        DecimalFormat dc = new DecimalFormat("###,###.00");

        for (int i = 0; i < groups.size(); i++) {
            int strength = groups.get(i).size();
            double sum = 0;
            double SR = 0;
            for(double d : groups.get(i)) {
                sum += d;
            }
            SR = sum / groups.get(i).size();
            String StringSR = dc.format(SR);
            line = line + "S/R Line " + (i + 1) + ": ";
            line = line + StringSR + " with Strength of " + strength + "<br>";
        }

        line = line + "</html>";
        System.out.println(line);

//        Gui.SR.setText(line);
    }
}