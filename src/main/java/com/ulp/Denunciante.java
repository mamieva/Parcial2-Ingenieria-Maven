package com.ulp;

import java.util.ArrayList;
import java.util.List;

/**
 * Persona que reporta una falla en un semáforo. Lleva el registro de todas
 * las denuncias que realizó.
 */
public class Denunciante {

    private String nombre;
    private String mail;
    private final List<Denuncia> misDenuncias = new ArrayList<>();

    public Denunciante(String nombre, String mail) {
        this.nombre = nombre;
        this.mail = mail;
    }

    public void registrarDenuncia(Denuncia d) {
        misDenuncias.add(d);
    }

    public String getMail() {
        return mail;
    }

    public String getNombre() {
        return nombre;
    }

    public List<Denuncia> getMisDenuncias() {
        return misDenuncias;
    }
}
