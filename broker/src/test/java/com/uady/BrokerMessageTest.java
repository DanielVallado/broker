package com.uady;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BrokerMessageTest {

    @Test
    public void testBrokerMessageMapping() {
        BrokerMessage brokerMessage = getBrokerMessage();

        assertEquals("registrar", brokerMessage.getServiceName());
        assertEquals(4, brokerMessage.getVariablesNumber());

        assertEquals("148.209.67.45", brokerMessage.getVariablesMap().get("servidor"));

        assertEquals("75", brokerMessage.getVariablesMap().get("puerto"));

        assertEquals("votar", brokerMessage.getVariablesMap().get("servicio"));

        assertEquals("1", brokerMessage.getVariablesMap().get("parametros"));
    }

    private static BrokerMessage getBrokerMessage() {
        String json = """
                {
                 "servicio" : "registrar",
                 "variables" : 4,
                 "variable1" : "servidor",
                 "valor1" : "148.209.67.45",
                 "variable2" : "puerto",
                 "valor2" : 75,
                 "variable3" : "servicio",
                 "valor3" : "votar",
                 "variable4" : "parametros",
                 "valor4" : 1
                }
                """;
        return new BrokerMessage(json);
    }

}
