package me.toyproject.loginjwt.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {

    @NotNull
    @Size(min = 5, max = 15)
    private String userName;

    @NotNull
    @Size(min = 6, max = 20)
    private String password;

}
