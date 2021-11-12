package org.jpmorgan.sssm;

import java.util.Map;

public class GBCEIndexCalculator {

    public static Double calculateAllShareIndex(Map<String, Stock> stocks) {
        Double allShareIndex = 0.0;
        for (Stock stock : stocks.values()) {
            allShareIndex += stock.getLastPrice();
        }
        return Math.pow(allShareIndex, 1.0 / stocks.size());
    }

    private GBCEIndexCalculator() {
        //Private constructor to hide
    }

}
