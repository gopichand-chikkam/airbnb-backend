package com.gopi.airbnb.dto.response;

import java.time.LocalDateTime;

public record ErrorResponse(int statusCode, String message, LocalDateTime timeStamp) {
}
