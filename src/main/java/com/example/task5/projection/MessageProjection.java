package com.example.task5.projection;

import org.springframework.beans.factory.annotation.Value;

public interface MessageProjection {

    Long getId();

    String getTitle();

    String getBody();

    @Value("#{@userRepo.getUserProjectionById(target.senderId)}")
    UserProjection getSender();

    @Value("#{@userRepo.getUserProjectionById(target.receiverId)}")
    UserProjection getReceiver();

    String getSentAt();
}
