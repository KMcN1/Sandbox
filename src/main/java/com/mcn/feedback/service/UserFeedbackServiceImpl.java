package com.mcn.feedback.service;


import com.mcn.common.types.BaseResponseDTO;
import com.mcn.feedback.dao.UserFeedbackDAO;
import com.mcn.feedback.model.dto.UserFeedbackDTO;
import com.mcn.feedback.model.dto.UserFeedbackResponseDTO;
import com.mcn.feedback.util.UserFeedbackFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.singletonList;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserFeedbackServiceImpl implements UserFeedbackService {

    private static final String MSG_SAVE_SUCCESS = "User feedback submitted";
    private static final String MSG_FIND_SUCCESS = "User feedback items found, size: %s";
    private static final String MSG_NO_DATA = "No user feedback found";
    private static final String MSG_DB_SAVE_ERROR = "An error occurred while attempting to save the User Feedback";
    private static final String MSG_DB_FIND_ERROR = "An error occurred while attempting to fetch the User Feedback";

    private final UserFeedbackDAO userFeedbackDAO;

    @Override
    public BaseResponseDTO submitFeedback(UserFeedbackDTO dto) {
        try {
            userFeedbackDAO.save(UserFeedbackFactory.createUserFeedbackFromDto(dto));
            return submitFeedbackSuccessResponse();
        } catch (Exception ex) {
            log.error(MSG_DB_SAVE_ERROR, ex);
            return databaseErrorResponse(ex, MSG_DB_SAVE_ERROR);
        }
    }

    @Override
    public UserFeedbackResponseDTO getAll() {
        try {
            if (userFeedbackDAO.count() == 0) {
                return noContentResponse();
            }
            List<UserFeedbackDTO> userFeedbackDTOList = createUserFeedbackDTOList();
            return UserFeedbackResponseDTO.builder()
                    .statusCode(HttpStatus.OK.value())
                    .userFeedbackList(userFeedbackDTOList)
                    .message(String.format(MSG_FIND_SUCCESS, userFeedbackDTOList.size()))
                    .build();
        } catch (Exception ex) {
            log.error(MSG_DB_FIND_ERROR, ex);
            return databaseErrorResponse(ex, MSG_DB_FIND_ERROR);
        }
    }

    private BaseResponseDTO submitFeedbackSuccessResponse() {
        return BaseResponseDTO.builder()
                .statusCode(HttpStatus.OK.value())
                .message(MSG_SAVE_SUCCESS)
                .build();
    }

    private UserFeedbackResponseDTO databaseErrorResponse(Exception ex, String message) {
        return UserFeedbackResponseDTO.builder()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .errors(singletonList(ex.getLocalizedMessage()))
                .message(message)
                .build();
    }

    private UserFeedbackResponseDTO noContentResponse() {
        return UserFeedbackResponseDTO.builder()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .message(MSG_NO_DATA)
                .build();
    }

    private List<UserFeedbackDTO> createUserFeedbackDTOList() {
        return userFeedbackDAO.findAll().stream()
                .map(UserFeedbackFactory::createDtoFromUserFeedback)
                .collect(Collectors.toList());
    }

}
