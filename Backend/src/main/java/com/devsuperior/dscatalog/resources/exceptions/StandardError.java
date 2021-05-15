package com.devsuperior.dscatalog.resources.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StandardError implements Serializable {
    private static final long serialVerionUID = 1L;

    private Instant timestamp;
    private Integer status;
    private String error;
    private String message;
    private String path;

    /*
    Exemplo de exibição do erro

    "timestamp": "2021-05-15T18:42:55.363+00:00",
     "status": 500,
     "error": "Internal Server Error",
     "message": "",
     "path": "/categories/7"
    */
}
