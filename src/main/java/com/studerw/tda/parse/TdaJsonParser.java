package com.studerw.tda.parse;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.studerw.tda.model.account.Order;
import com.studerw.tda.model.account.SecuritiesAccount;
import com.studerw.tda.model.history.PriceHistory;
import com.studerw.tda.model.instrument.FullInstrument;
import com.studerw.tda.model.instrument.Instrument;
import com.studerw.tda.model.marketdata.Mover;
import com.studerw.tda.model.markethours.Hours;
import com.studerw.tda.model.option.OptionChain;
import com.studerw.tda.model.quote.Quote;
import com.studerw.tda.model.transaction.Transaction;
import com.studerw.tda.model.user.Preferences;
import com.studerw.tda.model.user.StreamerSubscriptionKeys;
import com.studerw.tda.model.user.UserPrincipals;

import java.io.*;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TdaJsonParser {

  private static final Logger LOGGER = LoggerFactory.getLogger(TdaJsonParser.class);
  private static final TypeReference<LinkedHashMap<String, Quote>> MAPPED_QUOTE_TYPE_REF = new TypeReference<>() {};
  private static final TypeReference<List<Map<String, SecuritiesAccount>>> LIST_MAP_SEC_ACCT_TYPE_REF = new TypeReference<>() {};
  private static final TypeReference<List<Order>> LIST_ORDER_TYPE_REF = new TypeReference<>() {};
  private static final TypeReference<List<Instrument>> LIST_INSTRUMENT_TYPE_REF = new TypeReference<>() {};
  private static final TypeReference<Map<String, Instrument>> MAP_INSTRUMENT_TYPE_REF = new TypeReference<>() {
  };
  private static final TypeReference<Map<String, FullInstrument>> MAP_FULL_INSTRUMENT_TYPE_REFERENCE = new TypeReference<>() {
  };
  private static final TypeReference<List<Mover>> LIST_MOVER_TYPE_REF = new TypeReference<>() {
  };
  private static final TypeReference<OptionChain> OPTION_CHAIN_TYPE_REF = new TypeReference<>() {
  };
  private static final TypeReference<List<Transaction>> LIST_TRANS_TYPE_REF = new TypeReference<>() {
  };
  private static final TypeReference<Transaction> TRANSACTION_TYPE_REF = new TypeReference<>() {
  };
  private static final TypeReference<Preferences> PREFERENCES_TYPE_REF = new TypeReference<>() {
  };
  private static final TypeReference<UserPrincipals> USER_PRINCIPALS_TYPE_REF = new TypeReference<>() {
  };
  private static final TypeReference<StreamerSubscriptionKeys> STREAMER_SUBSCRIPTION_KEYS_TYPE_REF = new TypeReference<>() {
  };

  /**
   * @param in inputstream of JSON from rest call to TDA. The stream will be closed upon return.
   * @return list of objects that extend Quote.
   */
  public List<Quote> parseQuotes(InputStream in) {
    LOGGER.trace("parsing quotes...");
    try (BufferedInputStream bIn = new BufferedInputStream(in)) {
      LinkedHashMap<String, Quote> quotesMap = DefaultMapper.fromJson(bIn, MAPPED_QUOTE_TYPE_REF);
      LOGGER.debug("returned a map of size: {}", quotesMap.size());

      List<Quote> quotes = new ArrayList<>();
      quotesMap.forEach((k, v) -> quotes.add(v));
      return quotes;
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  /**
   * @param in {@link InputStream} of JSON from TDA; the stream will be closed upon return.
   * @return PriceHistory
   */
  public PriceHistory parsePriceHistory(InputStream in) {
    LOGGER.trace("parsing quotes...");
    try (BufferedInputStream bIn = new BufferedInputStream(in)) {
      final PriceHistory priceHistory = DefaultMapper.fromJson(bIn, PriceHistory.class);
      LOGGER.debug("returned a price history for {} of size: {}", priceHistory.getSymbol(),
          priceHistory.getCandles().size());
      return priceHistory;
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  /**
   * @param in {@link InputStream} of JSON from TDA; the stream will be closed upon return.
   * @return SecuritiesAccount
   */
  public SecuritiesAccount parseAccount(InputStream in) {
    LOGGER.trace("parsing securitiesAccount...");
    try (BufferedInputStream bIn = new BufferedInputStream(in)) {
      //Cannot use defaultMapper because account is wrapped in '{ securitiesAccount: {...} }'
      final ObjectMapper objMapper = new ObjectMapper();
      objMapper.enable(DeserializationFeature.UNWRAP_ROOT_VALUE);

      final SecuritiesAccount securitiesAccount = objMapper.readValue(bIn, SecuritiesAccount.class);
      LOGGER.debug("returned a securitiesAccount of type: {}",
          securitiesAccount.getClass().getName());
      return securitiesAccount;
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  /**
   * @param in {@link InputStream} of JSON from TDA; the stream will be closed upon return.
   * @return List of SecuritiesAccounts
   */
  public List<SecuritiesAccount> parseAccounts(InputStream in) {
    LOGGER.trace("parsing securitiesAccounts...");
    try (BufferedInputStream bIn = new BufferedInputStream(in)) {
//      ObjectMapper mapper = new ObjectMapper();
      List<SecuritiesAccount> accounts = new ArrayList<>();
//      List<Map<String, SecuritiesAccount>> maps = mapper.readValue(bIn, LIST_MAP_SEC_ACCT_TYPE_REF);
      List<Map<String, SecuritiesAccount>> maps = DefaultMapper.fromJson(bIn, LIST_MAP_SEC_ACCT_TYPE_REF);
      for (Map<String, SecuritiesAccount> map : maps) {
        if (map.size() != 1 && map.containsKey("securitiesAccount")) {
          throw new IllegalStateException("Expecting of json list of securitiesAccount");
        }
        SecuritiesAccount securitiesAccount = map.get("securitiesAccount");
        accounts.add(securitiesAccount);
      }

      LOGGER.debug("returned a a list of {} securitiesAccounts: {}", accounts.size(), accounts);
      return accounts;
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  public Order parseOrder(InputStream in) {
    LOGGER.trace("parsing order...");
    try (BufferedInputStream bIn = new BufferedInputStream(in)) {
      final Order order = DefaultMapper.fromJson(bIn, Order.class);
      LOGGER.debug("Returned order of id: {}", order.getOrderId());
      return order;
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  public List<Order> parseOrders(InputStream in) {
    LOGGER.trace("parsing orders...");
    try (BufferedInputStream bIn = new BufferedInputStream(in)) {
      final List<Order> orders= DefaultMapper.fromJson(bIn, LIST_ORDER_TYPE_REF);
      LOGGER.debug("Returned list of orders of size: {}", orders.size());
      return orders;
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  /**
   *
   * @param in of json that is either an array of instruments or a map keyed by symbol
   * @return a single parsed Instrument from the list
   */
  public Instrument parseInstrumentArraySingle(InputStream in) {
    LOGGER.trace("parsing instrument array...");
    try (BufferedInputStream bIn = new BufferedInputStream(in)) {
      List<Instrument> instruments = DefaultMapper.fromJson(bIn, LIST_INSTRUMENT_TYPE_REF);
      if (instruments.size() != 1) {
        throw new RuntimeException("Excepting a json array of Instruments from TDA");
      }
      Instrument instrument = instruments.get(0);
      LOGGER.debug("Returned instrument: {}", instrument);
      return instrument;
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  /**
   *
   * @param in of json that is either an array of instruments or a map keyed by symbol
   * @return list of instruments
   */
  public List<Instrument> parseInstrumentMap(InputStream in) {
    LOGGER.trace("parsing instrument map...");
    try (BufferedInputStream bIn = new BufferedInputStream(in)) {
      Map<String, Instrument> instruments = DefaultMapper.fromJson(bIn, MAP_INSTRUMENT_TYPE_REF);
      LOGGER.debug("Returned instruments map of size: {}", instruments.size());
      return new ArrayList<>(instruments.values());
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  /**
   *
   * @param in of json that is either an array of instruments or a map keyed by symbol
   * @return list of full instruments
   */
  public List<FullInstrument> parseFullInstrumentMap(InputStream in) {
    LOGGER.trace("parsing full instrument map...");
    try (BufferedInputStream bIn = new BufferedInputStream(in)) {
      Map<String, FullInstrument> instruments = DefaultMapper.fromJson(bIn, MAP_FULL_INSTRUMENT_TYPE_REFERENCE);
      LOGGER.debug("Returned full instruments map of size: {}", instruments.size());
      return new ArrayList<>(instruments.values());
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  public List<Mover> parseMovers(InputStream in) {
    LOGGER.trace("parsing movers...");
    try (BufferedInputStream bIn = new BufferedInputStream(in)) {
      final List<Mover> movers = DefaultMapper.fromJson(bIn, LIST_MOVER_TYPE_REF);
      LOGGER.debug("Returned list of movers of size: {}", movers.size());
      return movers;
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  public OptionChain parseOptionChain(InputStream in) {
    LOGGER.trace("parsing option chain...");
    try (BufferedInputStream bIn = new BufferedInputStream(in)) {
      final OptionChain optionChain = DefaultMapper.fromJson(bIn, OPTION_CHAIN_TYPE_REF);
      LOGGER.debug("Returned optionChain: {}", optionChain);
      return optionChain;
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  public List<Transaction> parseTransactions(InputStream in) {
    LOGGER.trace("parsing transactions...");
    try (BufferedInputStream bIn = new BufferedInputStream(in)) {
      final List<Transaction> transactions = DefaultMapper.fromJson(bIn, LIST_TRANS_TYPE_REF);
      LOGGER.debug("Returned transactions: {}", transactions);
      return transactions;
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  public Transaction parseTransaction(InputStream in) {
    LOGGER.trace("parsing transaction...");
    try (BufferedInputStream bIn = new BufferedInputStream(in)) {
      final Transaction transaction = DefaultMapper.fromJson(bIn, TRANSACTION_TYPE_REF);
      LOGGER.debug("Returned transaction: {}", transaction);
      return transaction;
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  public Preferences parsePreferences(InputStream in) {
    LOGGER.trace("parsing preferences...");
    try (BufferedInputStream bIn = new BufferedInputStream(in)) {
      final Preferences preferences = DefaultMapper.fromJson(bIn, PREFERENCES_TYPE_REF);
      LOGGER.debug("Returned preferences: {}", preferences);
      return preferences;
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  public UserPrincipals parseUserPrincipals(InputStream in) {
    LOGGER.trace("parsing userPrincipals...");
    try (BufferedInputStream bIn = new BufferedInputStream(in)) {
      final UserPrincipals userPrincipals = DefaultMapper.fromJson(bIn, USER_PRINCIPALS_TYPE_REF);
      LOGGER.debug("Returned userPrincipals: {}", userPrincipals);
      return userPrincipals;
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  public StreamerSubscriptionKeys parseSubscriptionKeys(InputStream in) {
    LOGGER.trace("parsing subscription keys...");
    try (BufferedInputStream bIn = new BufferedInputStream(in)) {
      final StreamerSubscriptionKeys streamerSubscriptionKeys = DefaultMapper
          .fromJson(bIn, STREAMER_SUBSCRIPTION_KEYS_TYPE_REF);
      LOGGER.debug("Returned subscription keys: {}", streamerSubscriptionKeys);
      return streamerSubscriptionKeys;
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

    public List<Hours> parseMarketHours(InputStream in) throws IOException {
      LOGGER.trace("parsing market hours...");
      List<Hours> hoursList = new ArrayList<>();
//        ObjectMapper mapper = new ObjectMapper();
      JsonNode rootNode = DefaultMapper.readTree(in);
      for (Iterator<JsonNode> itype = rootNode.elements(); itype.hasNext(); ) {
        JsonNode typeNode = itype.next();
        for (Iterator<JsonNode> icode = typeNode.elements(); icode.hasNext(); ) {
          JsonNode hoursNode = icode.next();
          Hours hours = DefaultMapper.treeToValue(hoursNode, Hours.class);
          if(hours != null) {
            hoursList.add(hours);
          }
        }
      }
      return hoursList;
    }

//  public <T> T parseTdaJson(InputStream in, Class<T> type){
//    try (BufferedInputStream bIn = new BufferedInputStream(in)) {
//      LOGGER.debug("parsing JSON input to type: {}", type.getName());
//      final T tdaPojo = DefaultMapper.fromJson(in, new TypeReference<T>(){});
//      return tdaPojo;
//    } catch (IOException e) {
//      e.printStackTrace();
//      throw new RuntimeException(e);
//    }
//  }
}
