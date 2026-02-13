package com.example.TareaFinal.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResponseBase<T> {
    private int codigo;
    private String mensaje;
    private T data;
}
