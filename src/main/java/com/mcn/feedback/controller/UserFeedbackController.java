package com.mcn.feedback.controller;


import com.mcn.common.types.BaseResponseDTO;
import com.mcn.common.utils.ControllerUtils;
import com.mcn.feedback.model.dto.UserFeedbackDTO;
import com.mcn.feedback.model.dto.UserFeedbackResponseDTO;
import com.mcn.feedback.service.UserFeedbackService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Api("BlackArrow User Feedback API")
@RestController
@RequestMapping(path = "/api/feedback")
@Validated
public class UserFeedbackController {

    private final UserFeedbackService userFeedbackService;

    private final ControllerUtils controllerUtils;

    public UserFeedbackController(UserFeedbackService userFeedbackService, ControllerUtils controllerUtils) {
        this.userFeedbackService = userFeedbackService;
        this.controllerUtils = controllerUtils;
    }

    @ApiOperation(value = "Submit User Feedback",
            response = BaseResponseDTO.class)
    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponseDTO> submitFeedback(@Valid @RequestBody UserFeedbackDTO request) {
        BaseResponseDTO response = userFeedbackService.submitFeedback(request);
        return new ResponseEntity<>(response, controllerUtils.getHttpStatusCode(response.getStatusCode()));
    }

    @ApiOperation(value = "Get All User Feedback",
            response = UserFeedbackResponseDTO.class)
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<UserFeedbackResponseDTO> getAllFeedback() {
        UserFeedbackResponseDTO response = userFeedbackService.getAll();
        return new ResponseEntity<>(response, controllerUtils.getHttpStatusCode(response.getStatusCode()));
    }

}
