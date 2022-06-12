package com.mcn.feedback.model.dto;

import com.mcn.common.types.BaseResponseDTO;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@ApiModel
public class UserFeedbackResponseDTO extends BaseResponseDTO {

    private List<UserFeedbackDTO> userFeedbackList;

}
