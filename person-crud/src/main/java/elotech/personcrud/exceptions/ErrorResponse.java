package elotech.personcrud.exceptions;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private String status;

    private int statusCode;

    private String message;

    private LocalDateTime timeStamp;
}