package org.jpmorgan.sssm;

import org.jpmorgan.sssm.utils.TradeType;
import org.jpmorgan.sssm.utils.Type;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

public class StockTest {

    Stock stockGIN;
    Stock stockJOE;
    Stock stockALE;

    @Before
    public void init() {
        stockGIN = new Stock("GIN", Type.PREFERRED, 10.0, 0.2, 100.0);
        stockJOE = new Stock("JOE", Type.COMMON, 13.0, 0.0, 250.0);
        stockALE = new Stock("ALE", Type.COMMON, 23.0, 0.0, 60.0);
    }

    @Test
    public void testDividend() {
        Double dividendGin = stockGIN.dividend(20.0);
        Double expectedDividendGin = stockGIN.getFixedDividend() * stockGIN.getParValue() / 20.0;
        Assert.assertEquals(expectedDividendGin, dividendGin);

        Double dividendJoe = stockJOE.dividend(10.0);
        Double expectedDividendJoe = stockJOE.getLastDividend() / 10.0;
        Assert.assertEquals(expectedDividendJoe, dividendJoe);
    }

    @Test
    public void testPERatio() {
        Double peRatioALE = stockALE.peRatio(1.0);
        Double expectedPeRatioALE = 1.0 / stockALE.getLastDividend();
        Assert.assertEquals(expectedPeRatioALE, peRatioALE, 0.0);
    }

    @Test
    public void testBuy() {
        Stock stock1 = new Stock("SWAP", Type.COMMON, 25.0, 0.0, 80.0);
        stock1.buy(5, 100.0);
        Double expectedPrice = 100.0;
        stock1.toString();
        Assert.assertEquals(expectedPrice, stock1.getLastPrice());
    }

    @Test
    public void testSell() {
        Stock stock1 = new Stock("SWAP", Type.COMMON, 25.0, 0.0, 80.0);
        stock1.buy(5, 150.0);
        Double expectedPrice = 150.0;
        stock1.toString();
        Assert.assertEquals(expectedPrice, stock1.getLastPrice());
    }

    @Test
    public void testGetLastPrice() {
        Stock stock1 = new Stock("SWAP", Type.COMMON, 25.0, 0.0, 80.0);
        stock1.buy(5, 150.0);
        stock1.sell(5, 190.0);
        Double expectedPrice = 190.0;
        Assert.assertEquals(expectedPrice, stock1.getLastPrice());
    }

    @Test
    public void testGetLastPriceZero() {
        Stock stock1 = new Stock("SWAP", Type.COMMON, 25.0, 0.0, 80.0);
        Double expectedPriceZero = 0.0;
        Assert.assertEquals(expectedPriceZero, stock1.getLastPrice());
    }

    @Test
    public void testCalculateVolumeWeightedStockPrice() {
        Stock stock1 = new Stock("JPM", Type.COMMON, 23.0, 0.0, 60.0);
        stock1.sell(2, 10.0);
        stock1.buy(2, 10.0);
        Double volumeWeightedStockPrice = stock1.calculateVolumeWeightedStockPrice(20);
        Assert.assertEquals(10.0, volumeWeightedStockPrice, 0.0);
        Date now = new Date();
        Date startTime = new Date(now.getTime() - (25 * 60 * 1000));
        stock1.getTrades().put(startTime, new Trade(TradeType.BUY, 1, 30.0));
        volumeWeightedStockPrice = stock1.calculateVolumeWeightedStockPrice(20);
        Double expectedVolumeWeightedStockPrice = 10.0;
        Assert.assertEquals(10.0, volumeWeightedStockPrice, 0.0);
    }

}
