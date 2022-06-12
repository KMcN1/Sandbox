package com.mcn.universum;

import com.mcn.common.types.OsType;
import com.mcn.containers.IntegrationTestBase;
import com.mcn.feedback.dao.UserFeedbackDAO;
import com.mcn.feedback.model.UserFeedback;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Sql(scripts = {"classpath:db/clear_data.sql"})
class HeroSpringDataJpaRepositoryIT extends IntegrationTestBase {

    @Autowired
    private UserFeedbackDAO userFeedbackDAO;

    @Test
    void findHeroesBySearchCriteria_SpringDataJpa() {
        userFeedbackDAO.save(UserFeedback.builder()
                        .appVersion("1.2.0")
                        .comment("It's rubbish")
                        .email("me@mail.com")
                        .feedbackReason("Cos it's rubbish")
                        .name("me")
                        .feedbackScreenName("Some screen")
                        .osType(OsType.ANDROID)
                .build());

        List<UserFeedback> feedback = userFeedbackDAO.findAll();

        assertThat(feedback).hasSize(1);
    }

}