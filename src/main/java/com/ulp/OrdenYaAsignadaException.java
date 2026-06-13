package com.ulp;

/**
 * Se lanza al intentar crear una segunda orden de composición sobre una
 * denuncia que ya tiene una orden asignada. Refleja la multiplicidad "1" del
 * UML: una denuncia se liga a una única orden.
 */
public class OrdenYaAsignadaException extends RuntimeException {

    public OrdenYaAsignadaException(String mensaje) {
        super(mensaje);
    }
}
