package com.uady.registry;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Server {

    private int id;
    private String ip;
    private int port;
    private String serviceName;
    private int numberParameters;

}
