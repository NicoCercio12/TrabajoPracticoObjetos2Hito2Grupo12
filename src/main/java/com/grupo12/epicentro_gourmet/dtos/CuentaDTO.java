package com.grupo12.epicentro_gourmet.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CuentaDTO {

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Ingresá un email válido")
    @Size(max = 100)
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "Ingresá una contraseña válida")
    private String password;

    @NotBlank(message = "Confirmá la contraseña")
    private String confirmarPassword;

}
