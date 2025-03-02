package com.pruebatecnica.Inditex.domain.exception;

/**
 * Excepción de dominio para representar que no se encontró un precio.
 */
public class PriceNotFoundException extends RuntimeException {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PriceNotFoundException(String message) {
        super(message);
    }
}
