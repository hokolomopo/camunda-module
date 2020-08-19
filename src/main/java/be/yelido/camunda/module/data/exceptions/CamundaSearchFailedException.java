package be.yelido.camunda.module.data.exceptions;

import java.io.IOException;

public class CamundaSearchFailedException extends RuntimeException {
    public CamundaSearchFailedException(String message) {
        super(message);
    }
}
