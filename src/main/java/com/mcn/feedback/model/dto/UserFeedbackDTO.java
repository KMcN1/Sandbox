package com.mcn.feedback.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mcn.common.types.OsType;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@ApiModel
@Data
@Builder
public class UserFeedbackDTO {

    @NotEmpty
    private String name;

    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    @Size(max = 500)
    private String comment;

    @NotNull
    @JsonProperty("os_type")
    private OsType osType;

    @NotEmpty
    @JsonProperty("app_version")
    private String appVersion;

    @NotEmpty
    @JsonProperty("feedback_screen_name")
    private String feedbackScreenName;

    @NotEmpty
    @JsonProperty("feedback_reason")
    private String feedbackReason;
}
