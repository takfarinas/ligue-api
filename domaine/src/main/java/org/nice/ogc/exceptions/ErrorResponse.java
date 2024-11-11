package org.nice.ogc.exceptions;

import java.time.LocalDateTime;

public record ErrorResponse(String message, String details, LocalDateTime timestamp) {
}
