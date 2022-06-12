package com.mcn.common.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;
import java.time.OffsetDateTime;

@MappedSuperclass
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public abstract class BaseEntity {

    @JsonIgnore
    @ApiModelProperty(notes = "Version number.")
    @Version
    private Integer version;

    @ApiModelProperty(notes = "Creation date of the record.")
    private OffsetDateTime dateCreated;

    @ApiModelProperty(notes = "Modification date of the record.")
    private OffsetDateTime lastUpdated;

    @PreUpdate
    @PrePersist
    private void updateTimeStamps() {
        lastUpdated = OffsetDateTime.now();
        if (dateCreated == null) {
            dateCreated = lastUpdated;
        }
    }
}
