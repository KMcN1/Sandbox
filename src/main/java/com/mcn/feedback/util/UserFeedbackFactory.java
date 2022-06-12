package com.mcn.feedback.util;


import com.mcn.feedback.model.UserFeedback;
import com.mcn.feedback.model.dto.UserFeedbackDTO;
import org.springframework.beans.BeanUtils;

public class UserFeedbackFactory {

    private UserFeedbackFactory() {
    }

    public static UserFeedback createUserFeedbackFromDto(UserFeedbackDTO dto) {
        UserFeedback userFeedback = new UserFeedback();
        BeanUtils.copyProperties(dto, userFeedback);
        return userFeedback;
    }

    public static UserFeedbackDTO createDtoFromUserFeedback(UserFeedback userFeedback) {
        UserFeedbackDTO dto = UserFeedbackDTO.builder().build(); // No default constructor
        BeanUtils.copyProperties(userFeedback, dto);
        return dto;
    }

}
