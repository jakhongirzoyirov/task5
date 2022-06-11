package com.example.task5.payload.req;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessageDto {

    @NotBlank
    String title;

    @NotBlank
    String body;

    @NotNull
    Long receiverId;

    Long senderId;
}
