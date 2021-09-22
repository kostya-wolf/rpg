package com.game.model.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Исключение, выбрасываемое, если объект не найден в базе
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(long id) {
        super("Player with id=" + id + " not found");
    }
}
