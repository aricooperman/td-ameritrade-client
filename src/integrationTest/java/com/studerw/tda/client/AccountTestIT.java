package com.studerw.tda.client;

import com.studerw.tda.model.account.SecuritiesAccount;
import com.studerw.tda.parse.Utils;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.Assertions;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class AccountTestIT extends BaseTestIT {

  private static final Logger LOGGER = LoggerFactory.getLogger(AccountTestIT.class);

  @Test(expected = Exception.class)
  public void testAccountNoId() {
    httpTdaClient.getAccount("", false, false);
    Assertions.fail("shouldn't get here");
  }

  @Test
  public void testAccountAlone() {
    String accountId = getAccountId();
    if (StringUtils.isBlank(accountId)) {
      LOGGER.warn("No accountID set in props - ignoring 'getAccount('accountId', ...)' test");
      return;
    }

    final SecuritiesAccount account = httpTdaClient.getAccount(getAccountId(), true, true);
    Assertions.assertThat(account).isNotNull();
    LOGGER.debug(account.toString());
    if (!Utils.isNullOrEmpty(account.getOtherFields())) {
      LOGGER.warn("some files weren't mapped: {}", account.getOtherFields());
    }
  }

  @Test
  public void testGetAccounts() {
    final List<SecuritiesAccount> accounts = httpTdaClient.getAccounts(true, true);
    for (SecuritiesAccount account : accounts) {
      if (!Utils.isNullOrEmpty(account.getOtherFields())) {
        LOGGER.warn("some files weren't mapped: {}", account.getOtherFields());
      }
    }
    accounts.forEach(a -> LOGGER.debug("{}", a));
  }

  @Test
  @Ignore
  public void testGetMainAccountId() {
    String fromProps = getAccountId();
    final List<SecuritiesAccount> accounts = httpTdaClient.getAccounts(false, false);
    String fromClient = accounts.get(0).getAccountId();
    Assertions.assertThat(fromProps).isEqualTo(fromClient);
    LOGGER.debug("{} should equal {}", fromProps, fromClient);
  }


}