package com.example.task5.entity;

import com.example.task5.entity.abs.AbsEntity;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "messages")
public class Message extends AbsEntity {

    @Column(nullable = false)
    String title;

    @Column(nullable = false)
    String body;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false, referencedColumnName = "id")
    User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false, referencedColumnName = "id")
    User receiver;

    @Column(nullable = false)
    UUID chatId;
}
