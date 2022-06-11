package com.example.task5.payload.resp;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApiResponse {

    boolean success;
    String message;
    Object data;

    public static ResponseEntity<ApiResponse> response(Object data) {
        return ResponseEntity.ok(new ApiResponse(true, null, data));
    }
}
