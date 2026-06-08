# RabbitMQ Messaging Lab

> Portfolio status: active backend messaging lab.

This repository is being rebuilt as a practical RabbitMQ lab for Java and
Spring Boot backend systems. The goal is to demonstrate asynchronous
communication, message routing, reliability patterns, and operational thinking.

## Why This Repository Exists

Backend systems often need to process work asynchronously: notifications,
billing events, audit logs, integrations, retries, and background jobs.
RabbitMQ is a strong tool for learning those concepts because it makes queues,
exchanges, routing keys, acknowledgements, and dead-letter behavior explicit.

This lab focuses on:

- RabbitMQ fundamentals;
- AMQP concepts;
- Spring Boot integration;
- producer and consumer design;
- exchange and queue routing;
- dead-letter queues;
- retry and failure handling;
- observability for messaging systems;
- small examples that can be explained in interviews.

## Current Scope

The existing `spring-rabbitMQ` folder is preserved as part of the repository
history and will be progressively reorganized into clearer labs.

Current sections:

- [Messaging Fundamentals](docs/messaging-fundamentals.md)
- [Repository Roadmap](docs/roadmap.md)

Planned sections:

- direct exchange example;
- topic exchange example;
- dead-letter queue example;
- retry strategy example;
- Spring Boot messaging service;
- test strategy for message-driven code.

## Portfolio Role

This is a supporting repository in my backend portfolio. It complements the
main Spring Boot API project by showing messaging knowledge and asynchronous
architecture concepts.

## Repository Principles

- Preserve learning history.
- Keep examples small and runnable.
- Explain the business scenario behind each message flow.
- Document trade-offs, not only commands.
- Use small commits with clear messages.

## Interview Talking Points

- When asynchronous messaging is useful.
- How exchanges route messages to queues.
- Why retries need limits and dead-letter handling.
- How message-driven systems fail.
- How producers and consumers stay decoupled.
