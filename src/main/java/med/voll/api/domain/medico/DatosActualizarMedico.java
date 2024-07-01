package med.voll.api.domain.medico;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import med.voll.api.domain.direccion.DatosDireccion;

public record DatosActualizarMedico(
        @NotNull
        Long id,
        @Pattern(regexp = "^(?!\\s*$).+")
        String nombre,
        @Pattern(regexp = "\\d{4,6}")
        String documento,
        DatosDireccion direccion
) {
}
