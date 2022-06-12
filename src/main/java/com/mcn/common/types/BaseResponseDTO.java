package com.mcn.common.types;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModel;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@ApiModel
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@JsonPropertyOrder({"status_code", "message", "errors", "description"})
@EqualsAndHashCode
@ToString
public class BaseResponseDTO {

    @JsonProperty("status_code")
    private int statusCode;
    private String message;
    private List<String> errors;
    private String description;
}
