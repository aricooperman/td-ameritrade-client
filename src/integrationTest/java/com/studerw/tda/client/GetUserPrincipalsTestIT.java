package com.studerw.tda.client;

import static org.assertj.core.api.Assertions.assertThat;

import com.studerw.tda.model.user.UserPrincipals;
import com.studerw.tda.model.user.UserPrincipals.Field;
import java.util.Map;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class GetUserPrincipalsTestIT extends BaseTestIT {

  private static final Logger LOGGER = LoggerFactory.getLogger(GetUserPrincipalsTestIT.class);

  @Test
  public void testGetUserPrincipals() {
    final UserPrincipals userPrincipals = httpTdaClient.getUserPrincipals();
    Assertions.assertThat(userPrincipals).isNotNull();
    LOGGER.debug("{}", userPrincipals);
  }

  @Test
  public void testGetUserPrincipalsPrefs() {
    final UserPrincipals userPrincipals = httpTdaClient.getUserPrincipals(Field.preferences);
    Assertions.assertThat(userPrincipals).isNotNull();
    Assertions.assertThat(userPrincipals.getStreamerSubscriptionKeys()).isNull();
    Assertions.assertThat(userPrincipals.getStreamerInfo()).isNull();
    Assertions.assertThat(userPrincipals.getAccounts().get(0).getPreferences()).isNotNull();
    final Map<String, String> surrogateIds = userPrincipals.getAccounts().get(0)
        .getSurrogateIds();
    Assertions.assertThat(surrogateIds).isNull();
    LOGGER.debug("{}", userPrincipals);
  }

  @Test
  public void testGetUserPrincipalsNoPrefs() {
    final UserPrincipals userPrincipals = httpTdaClient
        .getUserPrincipals(
            Field.streamerConnectionInfo,
            Field.surrogateIds,
            Field.streamerSubscriptionKeys,
            Field.preferences
        );
    Assertions.assertThat(userPrincipals).isNotNull();
    Assertions.assertThat(userPrincipals.getStreamerSubscriptionKeys()).isNotNull();
    Assertions.assertThat(userPrincipals.getStreamerInfo()).isNotNull();
    Assertions.assertThat(userPrincipals.getAccounts().get(0)
        .getSurrogateIds()).isNotNull();
    Assertions.assertThat(userPrincipals.getAccounts().get(0).getPreferences()).isNotNull();
    LOGGER.debug("{}", userPrincipals);
  }

}