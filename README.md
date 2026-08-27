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
- producer safeguards for unroutable messages;
- observability for messaging systems;
- small examples that can be explained in interviews.

## Current Scope

The existing `spring-rabbitMQ` folder is preserved as part of the repository
history and will be progressively reorganized into clearer labs.

The repository now includes a local `docker-compose.yml` for running RabbitMQ
with the management UI during development.

Current sections:

- [Messaging Fundamentals](docs/messaging-fundamentals.md)
- [Reviewer Scorecard](docs/reviewer-scorecard.md)
- [Technical Depth Map](docs/technical-depth-map.md)
- [Portfolio Positioning](docs/portfolio-positioning.md)
- [Hiring Manager Summary](docs/hiring-manager-summary.md)
- [30-Second Pitch](docs/30-second-pitch.md)
- [Interview Route](docs/interview-route.md)
- [Interview Case Study](docs/interview-case-study.md)
- [Key Talking Points](docs/key-talking-points.md)
- [Pre-Interview Checklist](docs/pre-interview-checklist.md)
- [Tough Interview Questions](docs/tough-interview-questions.md)
- [Senior Review Notes](docs/senior-review-notes.md)
- [Architecture Review Checklist](docs/architecture-review-checklist.md)
- [Business Value](docs/business-value.md)
- [Technical Debt Register](docs/technical-debt-register.md)
- [Decision Log](docs/decision-log.md)
- [Next Review Focus](docs/next-review-focus.md)
- [Demo Script](docs/demo-script.md)
- [Interview Questions](docs/interview-questions.md)
- [Direct Exchange Example](docs/direct-exchange.md)
- [Topic Exchange Example](docs/topic-exchange.md)
- [Dead-Letter Queue Example](docs/dead-letter-queue.md)
- [Message Replay](docs/message-replay.md)
- [Manual Acknowledgement](docs/manual-acknowledgement.md)
- [Consumer Error Handling](docs/consumer-error-handling.md)
- [Idempotency In Message Consumers](docs/idempotency.md)
- [Local RabbitMQ Setup](docs/local-rabbitmq-setup.md)
- [CI Workflow](docs/ci.md)
- [Messaging Observability](docs/observability.md)
- [Backpressure](docs/backpressure.md)
- [Topology Naming](docs/topology-naming.md)
- [Message Testing Strategy](docs/message-testing-strategy.md)
- [Message Contract Template](docs/message-contract-template.md)
- [Retry Strategy Example](docs/retry-strategy.md)
- [Repository Roadmap](docs/roadmap.md)
- [Changelog](CHANGELOG.md)

Planned sections:

- Spring Boot messaging service;

## Portfolio Role

This is a supporting repository in my backend portfolio. It complements the
main Spring Boot API project by showing messaging knowledge and asynchronous
architecture concepts.

## Interview Checkpoint

A reviewer should focus on routing, dead-letter handling, idempotency, and
producer safeguards. These topics show messaging reliability beyond simply
sending and consuming a message.

## Portfolio Proof

This repository proves asynchronous architecture awareness. It shows message
flow, failure handling, replay, idempotency, and producer reliability as
engineering concerns.

## Fast Review Path

1. Read the [30-Second Pitch](docs/30-second-pitch.md) to understand the repository signal.
2. Open the [Interview Case Study](docs/interview-case-study.md) to follow the messaging story.
3. Inspect DLQ, idempotency, retry, and producer safeguard docs to validate reliability thinking.

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
