package com.mcn.feedback.service;


import com.mcn.common.types.BaseResponseDTO;
import com.mcn.feedback.model.dto.UserFeedbackDTO;
import com.mcn.feedback.model.dto.UserFeedbackResponseDTO;

public interface UserFeedbackService {
    BaseResponseDTO submitFeedback(UserFeedbackDTO userFeedbackRequest);

    UserFeedbackResponseDTO getAll();
}
