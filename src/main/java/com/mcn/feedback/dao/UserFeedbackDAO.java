package com.mcn.feedback.dao;

import com.mcn.feedback.model.UserFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserFeedbackDAO extends JpaRepository<UserFeedback, UUID> {
}
