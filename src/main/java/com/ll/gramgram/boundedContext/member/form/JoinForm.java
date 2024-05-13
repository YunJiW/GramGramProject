package com.ll.gramgram.boundedContext.member.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class JoinForm {

    @NotBlank
    @Size(min = 4,max = 20)
    private final String username;

    @NotBlank
    @Size(min = 4, max = 30)
    private final String password;
}
