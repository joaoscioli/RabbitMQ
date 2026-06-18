# Messaging Observability

This document describes what should be observed in a RabbitMQ-based backend
system.

## Goal

Asynchronous systems can fail quietly if queues, retries, and consumers are not
visible. Observability helps the team understand whether messages are flowing,
where they are delayed, and which failures require action.

## Metrics To Watch

### Queue Depth

Queue depth shows how many messages are waiting to be consumed.

A growing queue can indicate:

- slow consumers;
- unavailable downstream systems;
- traffic spikes;
- consumer crashes;
- insufficient concurrency.

### Consumer Count

Consumer count shows whether expected workers are connected.

If the consumer count drops to zero, messages may continue to accumulate even
though producers are still publishing successfully.

### Publish Rate

Publish rate shows how many messages producers are sending.

It helps separate producer spikes from consumer-side problems.

### Ack And Nack Rate

Acknowledgement metrics show whether consumers are successfully processing
messages or rejecting them.

A high negative acknowledgement rate can indicate validation errors, transient
failures, or poison messages.

### Dead-Letter Queue Volume

Dead-letter queue growth should be treated as a signal that messages need
investigation.

The DLQ is not a trash can. It is an operational feedback mechanism.

## Logs

Consumer logs should include:

- message type;
- business identifier;
- correlation id;
- attempt number;
- failure reason;
- final handling decision.

Logs should not include secrets, full personal data, or sensitive payloads.

The sample Spring Boot consumer uses the application logger instead of
`System.out` so local examples follow the same direction expected in production
services.

## Alerts

Useful alerts include:

- queue depth above threshold for several minutes;
- no consumers connected to a critical queue;
- dead-letter queue receives messages;
- retry queue grows continuously;
- message processing latency exceeds the expected window.

## Portfolio Signal

Messaging observability shows that the engineer understands asynchronous
systems as production workflows, not only as local examples that publish and
consume a message once.
