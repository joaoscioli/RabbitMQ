package com.example.springrabbitMQ;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class DemoPublisherTests {
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
