package com.studerw.tda.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;

import com.studerw.tda.model.history.Candle;
import com.studerw.tda.model.history.FrequencyType;
import com.studerw.tda.model.history.PriceHistReq;
import com.studerw.tda.model.history.PriceHistReq.Builder;
import com.studerw.tda.model.history.PriceHistory;
import java.math.BigDecimal;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PriceHistoryTestIT extends BaseTestIT {

  private static final Logger LOGGER = LoggerFactory.getLogger(PriceHistoryTestIT.class);

  //"VTSAX", "MSFT", "NOK/JPY", "$SPX.X", "MSFT_061821P65", "SPY");

  @Test
  public void testPriceHistoryEquity() {
    long now = System.currentTimeMillis();
    PriceHistory priceHistory = httpTdaClient.priceHistory("msft");
    Assertions.assertThat(priceHistory).isNotNull();
    Assertions.assertThat(priceHistory.getCandles().size()).isGreaterThan(1000);
    Assertions.assertThat(priceHistory.getSymbol()).isEqualTo("MSFT");
    Assertions.assertThat(priceHistory.isEmpty()).isFalse();
    LOGGER.debug(priceHistory.toString());
    Candle candle = priceHistory.getCandles().get(10);
    LOGGER.debug(candle.toString());
    Assertions.assertThat(candle.getOpen()).isGreaterThan(new BigDecimal("1.00"));
    Assertions.assertThat(candle.getHigh()).isGreaterThan(new BigDecimal("1.00"));
    Assertions.assertThat(candle.getLow()).isGreaterThan(new BigDecimal("1.00"));
    Assertions.assertThat(candle.getClose()).isGreaterThan(new BigDecimal("1.00"));
    Assertions.assertThat(candle.getVolume()).isGreaterThan(0);
    //should not have taken more than 20 seconds
    Assertions.assertThat(candle.getDatetime() - now).isLessThan(20000);

  }

  @Test
  public void testPriceHistoryMutualFund() {
    long now = System.currentTimeMillis();
    PriceHistory priceHistory = httpTdaClient.priceHistory("VTSAX");
    Assertions.assertThat(priceHistory).isNotNull();
    Assertions.assertThat(priceHistory.getCandles().size()).isGreaterThan(5);
    Assertions.assertThat(priceHistory.getSymbol()).isEqualTo("VTSAX");
    Assertions.assertThat(priceHistory.isEmpty()).isFalse();
    LOGGER.debug(priceHistory.toString());
    Candle candle = priceHistory.getCandles().get(0);
    LOGGER.debug(candle.toString());
    Assertions.assertThat(candle.getOpen()).isGreaterThan(new BigDecimal("1.00"));
    Assertions.assertThat(candle.getHigh()).isGreaterThan(new BigDecimal("1.00"));
    Assertions.assertThat(candle.getLow()).isGreaterThan(new BigDecimal("1.00"));
    Assertions.assertThat(candle.getClose()).isGreaterThan(new BigDecimal("1.00"));
    //should not have taken more than 20 seconds
    Assertions.assertThat(candle.getDatetime() - now).isLessThan(20000);
  }

  @Test
  //throwing 400 bad request
  //TODO
  @Ignore
  public void testPriceHistoryForex() {
    long now = System.currentTimeMillis();
    PriceHistory priceHistory = httpTdaClient.priceHistory("NOK/JPY");
    Assertions.assertThat(priceHistory).isNotNull();
    Assertions.assertThat(priceHistory.getCandles().size()).isGreaterThan(10);
    Assertions.assertThat(priceHistory.getSymbol()).isEqualTo("NOK/JPY");
    Assertions.assertThat(priceHistory.isEmpty()).isFalse();
    LOGGER.debug(priceHistory.toString());
    Candle candle = priceHistory.getCandles().get(0);
    LOGGER.debug(candle.toString());
    Assertions.assertThat(candle.getVolume()).isGreaterThan(0);
    //should not have taken more than 20 seconds
    Assertions.assertThat(candle.getDatetime() - now).isLessThan(20000);
  }

  @Test
  public void testPriceHistoryIndex() {
    long now = System.currentTimeMillis();
    PriceHistory priceHistory = httpTdaClient.priceHistory("$SPX.X");
    Assertions.assertThat(priceHistory).isNotNull();
    Assertions.assertThat(priceHistory.getCandles().size()).isGreaterThan(10);
    Assertions.assertThat(priceHistory.getSymbol()).isEqualTo("$SPX.X");
    Assertions.assertThat(priceHistory.isEmpty()).isFalse();
    LOGGER.debug(priceHistory.toString());
    Candle candle = priceHistory.getCandles().get(0);
    LOGGER.debug(candle.toString());
//    assertThat(candle.getVolume()).isGreaterThan(0);
    //should not have taken more than 20 seconds
    Assertions.assertThat(candle.getDatetime() - now).isLessThan(20000);
  }

  @Test
  public void testPriceHistoryEtf() {
    long now = System.currentTimeMillis();
    PriceHistory priceHistory = httpTdaClient.priceHistory("SPY");
    Assertions.assertThat(priceHistory).isNotNull();
    Assertions.assertThat(priceHistory.getCandles().size()).isGreaterThan(10);
    Assertions.assertThat(priceHistory.getSymbol()).isEqualTo("SPY");
    Assertions.assertThat(priceHistory.isEmpty()).isFalse();
    LOGGER.debug(priceHistory.toString());
    Candle candle = priceHistory.getCandles().get(0);
    LOGGER.debug(candle.toString());
//    assertThat(candle.getVolume()).isGreaterThan(0);
    //should not have taken more than 20 seconds
    Assertions.assertThat(candle.getDatetime() - now).isLessThan(20000);
  }

  @Test
  @Ignore
  //this is returning EMPTY
  //TODO
  public void testPriceHistoryOption() {
    long now = System.currentTimeMillis();
    PriceHistory priceHistory = httpTdaClient.priceHistory("MSFT_061821P65");
    Assertions.assertThat(priceHistory).isNotNull();
    Assertions.assertThat(priceHistory.getCandles().size()).isGreaterThan(10);
    Assertions.assertThat(priceHistory.getSymbol()).isEqualTo("MSFT_061821P65");
    Assertions.assertThat(priceHistory.isEmpty()).isFalse();
    LOGGER.debug(priceHistory.toString());
    Candle candle = priceHistory.getCandles().get(0);
    LOGGER.debug(candle.toString());
//    assertThat(candle.getVolume()).isGreaterThan(0);
    //should not have taken more than 20 seconds
    Assertions.assertThat(candle.getDatetime() - now).isLessThan(20000);
  }


  @Test(expected = IllegalArgumentException.class)
  public void testPriceHistoryEmpty() {
    PriceHistory priceHistory = httpTdaClient.priceHistory(" ");
    Fail.fail("should never get here");
  }

  @Test
  public void testValidPriceHistory() {
    long now = System.currentTimeMillis();
    PriceHistReq request = Builder.priceHistReq().withSymbol("MSFT").build();
    PriceHistory priceHistory = httpTdaClient.priceHistory(request);
    Assertions.assertThat(priceHistory).isNotNull();
    Assertions.assertThat(priceHistory.getCandles().size()).isGreaterThan(1000);
    Assertions.assertThat(priceHistory.getSymbol()).isEqualTo("MSFT");
    Assertions.assertThat(priceHistory.isEmpty()).isFalse();
//    LOGGER.debug(priceHistory.toString());
    Candle candle = priceHistory.getCandles().get(10);
    LOGGER.debug(candle.toString());
    Assertions.assertThat(candle.getOpen()).isGreaterThan(new BigDecimal("1.00"));
    Assertions.assertThat(candle.getHigh()).isGreaterThan(new BigDecimal("1.00"));
    Assertions.assertThat(candle.getLow()).isGreaterThan(new BigDecimal("1.00"));
    Assertions.assertThat(candle.getClose()).isGreaterThan(new BigDecimal("1.00"));
    Assertions.assertThat(candle.getVolume()).isGreaterThan(0);
    //should not have taken more than 20 seconds
    Assertions.assertThat(candle.getDatetime() - now).isLessThan(20000);
  }


  @Test
  public void testValidPriceHistory2() {
    long now = System.currentTimeMillis();
    PriceHistReq request = Builder.priceHistReq()
        .withSymbol("VGIAX")
        .withStartDate(System.currentTimeMillis() - (1000 * 60 * 60 * 24 * 7))
        .withFrequencyType(FrequencyType.minute)
        .withFrequency(1)
        .build();

    PriceHistory priceHistory = httpTdaClient.priceHistory(request);
    Assertions.assertThat(priceHistory).isNotNull();
    Assertions.assertThat(priceHistory.getCandles().size()).isGreaterThan(1);
    Assertions.assertThat(priceHistory.getSymbol()).isEqualTo("VGIAX");
    Assertions.assertThat(priceHistory.isEmpty()).isFalse();
    LOGGER.debug(priceHistory.toString());

    Candle candle = priceHistory.getCandles().get(0);
    LOGGER.debug(candle.toString());
    Assertions.assertThat(candle.getOpen()).isGreaterThan(new BigDecimal("1.00"));
    Assertions.assertThat(candle.getHigh()).isGreaterThan(new BigDecimal("1.00"));
    Assertions.assertThat(candle.getLow()).isGreaterThan(new BigDecimal("1.00"));
    Assertions.assertThat(candle.getClose()).isGreaterThan(new BigDecimal("1.00"));
    //should not have taken more than 20 seconds
    Assertions.assertThat(candle.getDatetime() - now).isLessThan(20000);
  }

}