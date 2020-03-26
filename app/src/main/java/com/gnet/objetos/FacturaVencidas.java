package com.gnet.objetos;

public class FacturaVencidas {
    String DOCUMENTO,FECHA,FECHA_VENCE,DVencidos,SALDO_LOCAL,CLIENTE,NOMBRE;

    public String getCLIENTE() {
        return CLIENTE;
    }

    public void setCLIENTE(String CLIENTE) {
        this.CLIENTE = CLIENTE;
    }

    public String getNOMBRE() {
        return NOMBRE;
    }

    public void setNOMBRE(String NOMBRE) {
        this.NOMBRE = NOMBRE;
    }

    public String getDOCUMENTO() {
        return DOCUMENTO;
    }

    public void setDOCUMENTO(String DOCUMENTO) {
        this.DOCUMENTO = DOCUMENTO;
    }

    public String getFECHA() {
        return FECHA;
    }

    public void setFECHA(String FECHA) {
        this.FECHA = FECHA;
    }

    public String getFECHA_VENCE() {
        return FECHA_VENCE;
    }

    public void setFECHA_VENCE(String FECHA_VENCE) {
        this.FECHA_VENCE = FECHA_VENCE;
    }

    public String getDVencidos() {
        return DVencidos;
    }

    public void setDVencidos(String DVencidos) {
        this.DVencidos = DVencidos;
    }

    public String getSALDO_LOCAL() {
        return SALDO_LOCAL;
    }

    public void setSALDO_LOCAL(String SALDO_LOCAL) {
        this.SALDO_LOCAL = SALDO_LOCAL;
    }
}
