package com.mcn.integration;

import com.mcn.common.types.OsType;
import com.mcn.integration.base.IntegrationTestBase;
import com.mcn.feedback.dao.UserFeedbackDAO;
import com.mcn.feedback.model.UserFeedback;
import com.mcn.integration.base.IntegrationTestBaseV2;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql(scripts = {"classpath:db/clear_data.sql"})
class HeroRestControllerIT extends IntegrationTestBaseV2 {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserFeedbackDAO userFeedbackDAO;

    @Sql(scripts = {"classpath:db/clear_data.sql"})
    @Test
    void findHeroesBySearchCriteria_MockMvc() throws Exception {
        saveFeedback("1.5.1", OsType.IOS);
        saveFeedback("1.2.0", OsType.ANDROID);

        MvcResult result = mockMvc.perform(get("/api/feedback")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String json = result.getResponse().getContentAsString();

        assertAll(
                () -> MatcherAssert.assertThat(json, Matchers.containsString("IOS")),
                () -> MatcherAssert.assertThat(json, Matchers.containsString("ANDROID"))
        );
    }

    private UserFeedback saveFeedback(String appVersion, OsType ios) {
        return userFeedbackDAO.save(UserFeedback.builder()
                .appVersion(appVersion)
                .comment("It's rubbish")
                .email("me@mail.com")
                .feedbackReason("Cos it's rubbish")
                .name("me")
                .feedbackScreenName("Some screen")
                .osType(ios)
                .build());
    }
}