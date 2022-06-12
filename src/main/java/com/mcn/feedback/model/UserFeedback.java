package com.mcn.feedback.model;

import com.mcn.common.entities.BaseEntity;
import com.mcn.common.types.OsType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity(name = "UserFeedback")
@Table(name = "user_feedback")
@Getter
@Setter
@NoArgsConstructor
@ToString
@SuperBuilder
public class UserFeedback extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String comment;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OsType osType;

    @Column(nullable = false)
    private String appVersion;

    @Column(nullable = false)
    private String feedbackScreenName;

    @Column(nullable = false)
    private String feedbackReason;

}
