package org.jpmorgan.sssm;

import org.jpmorgan.sssm.utils.TradeType;
import org.jpmorgan.sssm.utils.Type;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;

public class GBCEIndexCalculatorTest {

    HashMap<String, Stock> db;

    @Before
    public void init() {
        db = new HashMap<String, Stock>();
        db.put("TEA", new Stock("TEA", Type.COMMON, 0.0, 0.0, 100.0));
        db.put("POP", new Stock("POP", Type.COMMON, 8.0, 0.0, 100.0));
        db.put("ALE", new Stock("ALE", Type.COMMON, 23.0, 0.0, 60.0));
        db.put("GIN", new Stock("GIN", Type.PREFERRED, 8.0, 0.2, 100.0));
        db.put("JOE", new Stock("JOE", Type.COMMON, 13.0, 0.0, 250.0));
    }

    @Test
    public void testCalculateAllShareIndex() {
        for (Stock stock : db.values()) {
            // Record some trades
            for (int i = 1; i <= 10; i++) {
                stock.buy(2, 2.0);
                stock.sell(2, 2.0);
            }
        }
        Double GBCEallShareIndex = GBCEIndexCalculator.calculateAllShareIndex(db);
        Assert.assertEquals(1.5848931924611136, GBCEallShareIndex, 0.0);
    }

}
