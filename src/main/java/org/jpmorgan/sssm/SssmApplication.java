package org.jpmorgan.sssm;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jpmorgan.sssm.utils.Type;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@SpringBootApplication
public class SssmApplication {

    private static final Log log = LogFactory.getLog(SssmApplication.class);

    @Bean
    Map<String, Stock> db() {
        HashMap<String, Stock> db = new HashMap<>();
        db.put("TEA", new Stock("TEA", Type.COMMON, 0.0, 0.0, 100.0));
        db.put("POP", new Stock("POP", Type.COMMON, 8.0, 0.0, 100.0));
        db.put("ALE", new Stock("ALE", Type.COMMON, 23.0, 0.0, 60.0));
        db.put("GIN", new Stock("GIN", Type.PREFERRED, 8.0, 0.2, 100.0));
        db.put("JOE", new Stock("JOE", Type.COMMON, 13.0, 0.0, 250.0));
        return db;
    }

    public static void main(String[] args) {
        SpringApplication.run(SssmApplication.class, args);

        ApplicationContext context =
                new AnnotationConfigApplicationContext(SssmApplication.class);
        int noOfTradesToSimulate = 15;
        //Initialize the Database
        Map<String, Stock> db = context.getBean("db", Map.class);

        for (Stock stock : db.values()) {
            //Add some Trades
            for (int i = 0; i < noOfTradesToSimulate; i++) {
                Random rQty = new Random();

                Integer qtyMin = 11;
                Integer qtyMax = 20;

                Double priceMin = 101.0;
                Double priceMax = 200.0;

                int randomQty = rQty.nextInt(qtyMax - qtyMin) + qtyMin;
                double randomPriceBuy = (Math.random() * ((priceMax - priceMin) + 1)) + priceMin;
                stock.buy(randomQty, randomPriceBuy);
                System.out.println("Stock " + stock.getStockSymbol() + " bought " + randomQty + " shares at " + randomPriceBuy);

                double randomPriceSell = (Math.random() * ((priceMax - priceMin) + 1)) + priceMin;
                stock.sell(randomQty, randomPriceSell);
                System.out.println("Stock " + stock.getStockSymbol() + " sold " + randomQty + " shares at " + randomPriceSell);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    log.error(e.getMessage(), e);
                    Thread.currentThread().interrupt();
                }
            }
            System.out.println(stock.getStockSymbol() + " price: " + stock.getLastPrice());
            System.out.println(stock.getStockSymbol() + " volumeWeightedStockPrice: " + stock.calculateVolumeWeightedStockPrice(15));
        }
        Double gbceAllShareIndex = GBCEIndexCalculator.calculateAllShareIndex(db);
        System.out.println("GBCE All Share Index: " + gbceAllShareIndex);
    }

}
