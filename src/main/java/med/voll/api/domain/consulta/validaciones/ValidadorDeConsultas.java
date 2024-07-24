package med.voll.api.domain.consulta.validaciones;

import med.voll.api.domain.consulta.DatosAgendarConsulta;

public interface ValidadorDeConsultas {
    void validar(DatosAgendarConsulta datos);
}
