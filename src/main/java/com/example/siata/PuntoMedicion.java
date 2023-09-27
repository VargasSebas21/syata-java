package com.example.siata;

public class PuntoMedicion {
    public Double latitud, longitud;
    public int concentracion;

    public PuntoMedicion(Double latitud, Double longitud, int concentracion){
        this.latitud = latitud;
        this.longitud = longitud;
        this.concentracion = concentracion;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public int getConcentracion() {
        return concentracion;
    }

    public void setConcentracion(int concentracion) {
        this.concentracion = concentracion;
    }
}
