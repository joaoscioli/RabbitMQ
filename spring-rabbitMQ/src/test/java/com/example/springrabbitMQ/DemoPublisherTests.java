package com.example.springrabbitMQ;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class DemoPublisherTests {
    @Test
    void brokerFailureReturnsSafe503AndExplicitNextRequestCanRecover() throws Exception {
        for (String path : new String[]{"/", "/teste"}) {
            var sender = mock(QueueSender.class);
            doThrow(new org.springframework.amqp.AmqpConnectException(new java.net.ConnectException("private-broker")))
                    .doNothing().when(sender).send("test message");
            var mvc = MockMvcBuilders.standaloneSetup(new TesteController(sender)).build();

            mvc.perform(get(path)).andExpect(status().isServiceUnavailable())
                    .andExpect(content().string("Message publishing is unavailable"));
            verify(sender).send("test message");
            mvc.perform(get(path)).andExpect(status().isOk());
            verify(sender, times(2)).send("test message");
            verifyNoMoreInteractions(sender);
        }
    }

    @Test
    void compatibilityRoutesUseTheConfiguredSenderExactlyOncePerRequest() throws Exception {
        var sender = mock(QueueSender.class);
        var mvc = MockMvcBuilders.standaloneSetup(new TesteController(sender)).build();
        mvc.perform(get("/")).andExpect(status().isOk()).andExpect(content().string("ok. done"));
        mvc.perform(get("/teste")).andExpect(status().isOk()).andExpect(content().string("ok. foi"));
        verify(sender, times(2)).send("test message");
        verifyNoMoreInteractions(sender);
    }
}
