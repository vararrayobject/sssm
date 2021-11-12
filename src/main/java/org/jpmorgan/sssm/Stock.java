package org.jpmorgan.sssm;

import org.jpmorgan.sssm.utils.TradeType;
import org.jpmorgan.sssm.utils.Type;

import java.util.Date;
import java.util.SortedMap;
import java.util.TreeMap;

public class Stock {
    private String stockSymbol;
    private Type type;
    private Double lastDividend;
    private Double fixedDividend;
    private Double parValue;
    private TreeMap<Date, Trade> trades;

    public Stock(String stockSymbol, Type type, Double lastDividend, Double fixedDividend, Double parValue) {
        this.stockSymbol = stockSymbol;
        this.type = type;
        this.lastDividend = lastDividend;
        this.fixedDividend = fixedDividend;
        this.parValue = parValue;
        this.trades = new TreeMap<>();
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Double getLastDividend() {
        return lastDividend;
    }

    public void setLastDividend(Double lastDividend) {
        this.lastDividend = lastDividend;
    }

    public Double getFixedDividend() {
        return fixedDividend;
    }

    public void setFixedDividend(Double fixedDividend) {
        this.fixedDividend = fixedDividend;
    }

    public Double getParValue() {
        return parValue;
    }

    public void setParValue(Double parValue) {
        this.parValue = parValue;
    }

    public TreeMap<Date, Trade> getTrades() {
        return trades;
    }

    public void setTrades(TreeMap<Date, Trade> trades) {
        this.trades = trades;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "stockSymbol='" + stockSymbol + '\'' +
                ", type=" + type +
                ", lastDividend=" + lastDividend +
                ", fixedDividend=" + fixedDividend +
                ", parValue=" + parValue +
                '}';
    }

    /**
     * Calculate the Dividend yield when price is known
     *
     * @param price Price to be used to calculate the Dividend
     * @return Dividend
     */
    public Double dividend(Double price) {
        switch (this.getType()) {
            case COMMON:
                return this.getLastDividend() / price;
            case PREFERRED:
                return this.getFixedDividend() * this.getParValue() / price;
            default:
                return 0.0;
        }
    }

    /**
     * Calculate the PE Ration when price is known
     *
     * @param price Price to be used to calculate the Dividend
     * @return PE Ratio
     */
    public Double peRatio(Double price) {
        return price / this.getLastDividend();
    }

    /**
     * Buy stock by using quantity and price
     *
     * @param quantity Quantity of stock to sell
     * @param price    Price of stock
     */
    public void buy(Integer quantity, Double price) {
        Trade t = new Trade(TradeType.BUY, quantity, price);
        this.trades.put(new Date(), t);
    }

    /**
     * Sell stock by using quantity and price
     *
     * @param quantity Quantity of stock to sell
     * @param price    Price of stock
     */
    public void sell(Integer quantity, Double price) {
        Trade t = new Trade(TradeType.SELL, quantity, price);
        this.trades.put(new Date(), t);
    }

    /**
     * Get the current price of the Stock
     *
     * @return LTP or the current stock price
     */
    public Double getLastPrice() {
        if (this.trades.size() > 0) {
            return this.trades.lastEntry().getValue().getPrice();
        } else {
            return 0.0;
        }
    }

    /**
     * Calculate Volume Weighted Stock Price (VWSP)
     *
     * @param timeInMinutes The time for which VWSP is needed
     * @return Volume Weighted Stock Price
     */
    public Double calculateVolumeWeightedStockPrice(int timeInMinutes) {
        Date currentTime = new Date();
        Date startTime = new Date(currentTime.getTime() - (timeInMinutes * 60 * 1000));
        //Get the trades from startTime in a separate Map
        SortedMap<Date, Trade> allTrades = this.trades.tailMap(startTime);

        //Calculate the VWSP
        Double volumeWeightedStockPrice = 0.0;
        Integer totalQuantity = 0;

        for (Trade t : allTrades.values()) {
            totalQuantity += t.getQuantity();
            volumeWeightedStockPrice += t.getPrice() * t.getQuantity();
        }
        if (totalQuantity != 0) {
            return volumeWeightedStockPrice / totalQuantity;
        } else {
            return 0d;
        }
    }

}
