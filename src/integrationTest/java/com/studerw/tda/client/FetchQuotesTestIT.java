package com.studerw.tda.client;

import static org.assertj.core.api.Assertions.assertThat;

import com.studerw.tda.model.AssetType;
import com.studerw.tda.model.quote.EquityQuote;
import com.studerw.tda.model.quote.EtfQuote;
import com.studerw.tda.model.quote.ForexQuote;
import com.studerw.tda.model.quote.IndexQuote;
import com.studerw.tda.model.quote.MutualFundQuote;
import com.studerw.tda.model.quote.OptionQuote;
import com.studerw.tda.model.quote.Quote;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@SuppressWarnings("ConstantConditions")
public class FetchQuotesTestIT extends BaseTestIT {

  private static final Logger LOGGER = LoggerFactory.getLogger(FetchQuotesTestIT.class);

  @Test
  public void testStockQuote() {
    final Quote quote = BaseTestIT.httpTdaClient.fetchQuote("msft");
    Assertions.assertThat(quote.getAssetType()).isEqualTo(AssetType.EQUITY);
    Assertions.assertThat(quote).isInstanceOf(EquityQuote.class);
    Assertions.assertThat(quote.getSymbol()).isEqualToIgnoringCase("msft");
    EquityQuote equityQuote = (EquityQuote) quote;
    Assertions.assertThat(equityQuote).isNotNull();
    Assertions.assertThat(equityQuote.getAskPrice()).isGreaterThanOrEqualTo(BigDecimal.ONE);
  }

  @Test
  public void testStockQuotes() {
    List<String> stocks = Arrays.asList("VTSAX", "MSFT", "NOK/JPY", "$SPX.X", "MSFT_061821P65", "SPY");
    final List<Quote> quotes = BaseTestIT.httpTdaClient.fetchQuotes(stocks);
    Assertions.assertThat(quotes).size().isEqualTo(6);
    Assertions.assertThat(quotes.get(0).getAssetType()).isEqualTo(AssetType.MUTUAL_FUND);
    Assertions.assertThat(quotes.get(0)).isInstanceOf(MutualFundQuote.class);
    Assertions.assertThat(quotes.get(1).getAssetType()).isEqualTo(AssetType.EQUITY);
    Assertions.assertThat(quotes.get(1)).isInstanceOf(EquityQuote.class);
    Assertions.assertThat(quotes.get(2).getSymbol()).isEqualTo("NOK/JPY");
    Assertions.assertThat(quotes.get(2).getAssetType()).isEqualTo(AssetType.FOREX);
    Assertions.assertThat(quotes.get(2)).isInstanceOf(ForexQuote.class);
    Assertions.assertThat(quotes.get(3)).isInstanceOf(IndexQuote.class);
    Assertions.assertThat(quotes.get(4)).isInstanceOf(OptionQuote.class);
    Assertions.assertThat(quotes.get(5)).isInstanceOf(EtfQuote.class);
  }

  @Test
  public void testMutualFundQuotes() {
    List<String> mfs = Arrays.asList("VFIAX", "VTSAX");
    final List<Quote> quotes = BaseTestIT.httpTdaClient.fetchQuotes(mfs);
    Assertions.assertThat(quotes.size()).isEqualTo(2);

    MutualFundQuote mfq1 = (MutualFundQuote) quotes.get(0);
    Assertions.assertThat(mfq1.getAssetType()).isEqualTo(AssetType.MUTUAL_FUND);
    Assertions.assertThat(mfq1.getSymbol()).isEqualTo("VFIAX");
    LOGGER.debug(mfq1.toString());

    MutualFundQuote mfq2 = (MutualFundQuote) quotes.get(1);
    Assertions.assertThat(mfq2.getAssetType()).isEqualTo(AssetType.MUTUAL_FUND);
    Assertions.assertThat(mfq2.getSymbol()).isEqualTo("VTSAX");
    LOGGER.debug(mfq2.toString());
  }

  @Test
  public void testIndexQuotes() {
    Quote quote = BaseTestIT.httpTdaClient.fetchQuote("$SPX.X");
    Assertions.assertThat(quote instanceof IndexQuote);
    assert quote instanceof IndexQuote;
    IndexQuote indexQuote = (IndexQuote) quote;
    Assertions.assertThat(indexQuote.getSymbol()).isEqualTo("$SPX.X");
    Assertions.assertThat(indexQuote.getAssetType()).isEqualTo(AssetType.INDEX);
    LOGGER.debug(indexQuote.toString());
  }

  //These will eventually expire and be invalid
  @Test
  public void testOptionQuote() {
    final Quote quote = BaseTestIT.httpTdaClient.fetchQuote("MSFT_061821C120");
    Assertions.assertThat(quote instanceof OptionQuote);
    assert quote instanceof OptionQuote;
    OptionQuote optionQuote = (OptionQuote) quote;
    Assertions.assertThat(optionQuote.getSymbol()).isEqualTo("MSFT_061821C120");
    Assertions.assertThat(optionQuote.getAssetType()).isEqualTo(AssetType.OPTION);
    LOGGER.debug(optionQuote.toString());
  }

  @Test
  public void testEtfQuote() {
    final Quote quote = BaseTestIT.httpTdaClient.fetchQuote("SPY");
    Assertions.assertThat(quote instanceof EtfQuote);
    assert quote instanceof EtfQuote;
    EtfQuote etfQuote= (EtfQuote) quote;
    Assertions.assertThat(etfQuote.getSymbol()).isEqualTo("SPY");
    Assertions.assertThat(etfQuote.getAssetType()).isEqualTo(AssetType.ETF);
    LOGGER.debug(etfQuote.toString());
  }

  @Test
  public void testIssueQuote(){
    final List<Quote> quotes = BaseTestIT.httpTdaClient.fetchQuotes(Arrays.asList("FNDF", "FNDE"));
    Assertions.assertThat(quotes).hasSize(2);
    quotes.forEach(q -> LOGGER.debug("{}", q));
  }

  @Test
  @Ignore
  //https://github.com/studerw/td-ameritrade-client/issues/24
  public void testOptionChainMarkVsTov(){
    String chain = "WYNN_080720P73.5";
    final Quote quote = BaseTestIT.httpTdaClient.fetchQuote(
        chain);
    Assertions.assertThat(quote instanceof OptionQuote);
    OptionQuote optionQuote = (OptionQuote)quote;

    BigDecimal askPrice = optionQuote.getAskPrice();
    BigDecimal bidPrice = optionQuote.getBidPrice();
    LOGGER.debug("Bid: {}, Ask: {}", bidPrice, askPrice);

    LOGGER.debug("Mark: {}, theoreticalOptionValue: {}", optionQuote.getMark(),
        optionQuote.getTheoreticalOptionValue());
  }

  //We want to make sure a token is generated only once per client
  //Turn on full logging in logback-test.xml set:
  //logger  name="TDA_HTTP" level="debug"
  //logger name="com.studerw.tda.client.OauthInterceptor" level="debug"
  //
  //You will see that the call to get the first quote has a header: Authorization: Bearer UNSET
  //That triggers a 401 resposne from the server which the OAuthInterceptor detects and then makes a call for new auth token.
  //On all subsequent calls, you see that the Bearer header is now using the new auth token, and thus the
  //interceptor is never forced to create a new refresh token.
  @Test
  public void testOAuthCreatedOnlyOnce(){
    List<String> symbols = Arrays
        .asList("AAPL", "MSFT", "AMZN", "META", "GOOGL", "GOOG", "BRK.B", "JNJ", "V", "PG");

    for (String symbol : symbols) {
      final Quote quote = BaseTestIT.httpTdaClient.fetchQuote(symbol);
      Assertions.assertThat(quote).isNotNull();
      Assertions.assertThat(quote.getSymbol()).isEqualTo(symbol);
      LOGGER.debug("{} - {}", quote.getSymbol(), quote.getDescription());
    }
  }
}