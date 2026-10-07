package com.example.springrabbitMQ;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MessageControllerTests {
    private QueueSender sender;
    private MockMvc mvc;

    @BeforeEach
    void setup() {
        sender = mock(QueueSender.class);
        mvc = MockMvcBuilders.standaloneSetup(new MessageController(sender)).build();
    }

    @Test
    void repeatedRequestsPreserveTheCallerEventIdAndPayload() throws Exception {
        for (int attempt = 0; attempt < 2; attempt++) {
            mvc.perform(post("/messages").contentType(MediaType.APPLICATION_JSON)
                            .content("{\"payload\":\"order-123\",\"messageId\":\"event-456\"}"))
                    .andExpect(status().isAccepted());
        }
        verify(sender, times(2)).send("order-123", "event-456");
        verifyNoMoreInteractions(sender);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "{}", "null", "{", "{\"payload\":\"order\"}", "{\"messageId\":\"event\"}",
            "{\"payload\":\"\",\"messageId\":\"event\"}",
            "{\"payload\":\"order\",\"messageId\":\" \"}",
            "{\"payload\":\" \",\"messageId\":\"event\"}"
    })
    void rejectsInvalidBodiesWithoutPublishing(String body) throws Exception {
        mvc.perform(post("/messages").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(sender);
    }
}
