package es.kiwi.prices.domain.exception;

public class PricesNotFoundException extends RuntimeException{

    public PricesNotFoundException(String msg) {
        super(msg);
    }
}