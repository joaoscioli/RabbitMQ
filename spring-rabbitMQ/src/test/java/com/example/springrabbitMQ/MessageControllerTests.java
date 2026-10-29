package com.example.springrabbitMQ;

import org.springframework.amqp.AmqpConnectException;
import org.springframework.web.server.ResponseStatusException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;
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

    @ParameterizedTest
    @ValueSource(strings = {"event with spaces", "event/123", "café"})
    void rejectsUnsafeEventIdsWithoutCallingTheSender(String messageId) throws Exception {
        mvc.perform(post("/messages").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"payload\":\"order\",\"messageId\":\"" + messageId + "\"}"))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(sender);
    }

    @Test
    void validatesTheEventIdLengthAtTheHttpBoundary() throws Exception {
        var boundary = "x".repeat(128);
        mvc.perform(post("/messages").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"payload\":\"order\",\"messageId\":\"" + boundary + "\"}"))
                .andExpect(status().isAccepted());
        verify(sender).send("order", boundary);
        mvc.perform(post("/messages").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"payload\":\"order\",\"messageId\":\"" + boundary + "x\"}"))
                .andExpect(status().isBadRequest());
        verifyNoMoreInteractions(sender);
    }

    @Test
    void brokerFailureReturnsUnavailableWithoutRetryingOrExposingTheDiagnostic() throws Exception {
        var failure = new AmqpConnectException(new java.net.ConnectException("private broker address"));
        doThrow(failure).when(sender).send("order-123", "event-456");

        var result = mvc.perform(post("/messages").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"payload\":\"order-123\",\"messageId\":\"event-456\"}"))
                .andExpect(status().isServiceUnavailable()).andReturn();

        assertThat(result.getResolvedException()).isInstanceOf(ResponseStatusException.class);
        var exception = (ResponseStatusException) result.getResolvedException();
        assertThat(exception.getReason()).isEqualTo("Message publishing is unavailable");
        assertThat(exception.getCause()).isSameAs(failure);
        assertThat(result.getResponse().getContentAsString()).doesNotContain("private broker address");
        assertThat(result.getResponse().getErrorMessage()).doesNotContain("private broker address");
        verify(sender).send("order-123", "event-456");
        verifyNoMoreInteractions(sender);
    }

    @Test
    void explicitReplayAfterBrokerRecoveryPreservesTheOriginalEventIdentity() throws Exception {
        var failure = new AmqpConnectException(new java.net.ConnectException("private broker address"));
        doThrow(failure).doNothing().when(sender).send("order-123", "event-456");
        var body = "{\"payload\":\"order-123\",\"messageId\":\"event-456\"}";

        mvc.perform(post("/messages").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isServiceUnavailable());
        verify(sender).send("order-123", "event-456");
        verifyNoMoreInteractions(sender);

        // Caller replay, rather than an implicit retry of an uncertain publish outcome.
        mvc.perform(post("/messages").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isAccepted());
        verify(sender, times(2)).send("order-123", "event-456");
        verifyNoMoreInteractions(sender);
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
