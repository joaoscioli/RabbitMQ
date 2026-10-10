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

The consumer rejects null, empty, and whitespace-only payloads with
`AmqpRejectAndDontRequeueException` so malformed messages do not loop through
redelivery. The source queue routes these rejections to its durable DLQ.
Broker-free `QueueConsumerTests` cover the rejection contract;
broker acknowledgement and DLQ delivery require integration tests.

The simple listener allows at most three total handler attempts, with 100ms
then 200ms backoff. Exhausted failures are rejected without requeue and use the
same DLQ route. `ConsumerRetryTests` exercise the auto-configured retry advice
without a broker, including recovery on attempt two and rejection after three.
The current logging consumer has no durable business side effect; handlers added
later must implement durable idempotency before processing real events.
Accepted-message logs include only payload character count, keeping private body
content and embedded newlines out of logs. A broker-free output-capture test
checks this privacy boundary; character count is not an AMQP byte-size metric.

`queue.name` is the single source for the durable queue declaration, direct
exchange binding, producer destination, and consumer listener. Broker-free
`QueueTopologyTests` exercise an overridden queue name; live routing still
requires a running RabbitMQ broker.

Current sections:

- [Messaging Fundamentals](docs/messaging-fundamentals.md)
- [Portfolio Review Index](docs/portfolio-review-index.md)
- [Evidence Map](docs/evidence-map.md)
- [Engineering Impact](docs/engineering-impact.md)
- [Technical Scope](docs/technical-scope.md)
- [Reviewer Entrypoint](docs/reviewer-entrypoint.md)
- [Next Demo Slice](docs/next-demo-slice.md)
- [Technical Risks](docs/technical-risks.md)
- [Implementation Priority](docs/implementation-priority.md)
- [Acceptance Criteria](docs/acceptance-criteria.md)
- [Demo Readiness Checklist](docs/demo-readiness-checklist.md)
- [Reviewer Question Bank](docs/reviewer-question-bank.md)
- [Interview Defense Notes](docs/interview-defense-notes.md)
- [Seniority Evidence](docs/seniority-evidence.md)
- [Recruiter Summary](docs/recruiter-summary.md)
- [Technical Elevator Pitch](docs/technical-elevator-pitch.md)
- [Technical Review Map](docs/technical-review-map.md)
- [Interview Evaluation Criteria](docs/interview-evaluation-criteria.md)
- [Deep Dive Prompts](docs/deep-dive-prompts.md)
- [Hiring Signal](docs/hiring-signal.md)
- [Technical Differentiators](docs/technical-differentiators.md)
- [Next Technical Evolution](docs/next-technical-evolution.md)
- [Implementation Readiness](docs/implementation-readiness.md)
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
- [Production Readiness Matrix](docs/production-readiness-matrix.md)
- [Demo Evaluation Rubric](docs/demo-evaluation-rubric.md)
- [Maintenance Plan](docs/maintenance-plan.md)
- [Repository Health Scorecard](docs/repository-health-scorecard.md)
- [Reviewer FAQ](docs/reviewer-faq.md)
- [Interview Red Flags](docs/interview-red-flags.md)
- [Technical Storytelling Guide](docs/technical-storytelling-guide.md)
- [Next 90 Days Roadmap](docs/next-90-days-roadmap.md)
- [Hiring Manager One-Pager](docs/hiring-manager-one-pager.md)
- [Repository Maturity Levels](docs/repository-maturity-levels.md)
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

## Runnable Dead-Letter Topology

The Spring example declares a durable `${queue.name}.dlq` and configures the
source queue to dead-letter rejected messages through the default exchange
using the DLQ name as routing key. Blank payloads rejected by `QueueConsumer`
can therefore be retained for inspection. No automatic DLQ consumer or replay
is configured, preventing an endless rejection loop.

RabbitMQ cannot redeclare an existing queue with different arguments. For this
example, select a fresh `queue.name` before starting against a broker that
already has the old declaration; a production rollout needs a planned queue
migration or broker policy. Do not remove existing queues containing messages.

`QueueTopologyTests` verifies declarations, routing arguments, durability and
producer destination without a broker. Actual dead-letter delivery still needs
a broker integration test; this does not implement retry or idempotency.

## Message IDs for Replay

`QueueSender.send(payload)` assigns a new UUID to the AMQP `messageId` property.
Call `send(payload, "event-456")` with a stable application event ID when replaying
the same event; the producer preserves that ID and the payload. Blank IDs fail
before publishing. `QueueSenderTests` checks generated IDs, replay ID stability,
and unchanged message bytes without a broker. Consumers still need durable
deduplication storage before this can provide idempotent processing; generating
a fresh ID for each replay would defeat deduplication.

## Publish with a stable event ID

`POST /messages` accepts JSON such as `{"payload":"order-123","messageId":"event-456"}`
and returns HTTP 202 after the sender call completes. Missing or blank fields and malformed
JSON return HTTP 400 before publishing. Repeating the request publishes again with the same
AMQP message ID, allowing a future durable consumer to recognize a replay. This endpoint
does not deduplicate requests, confirm broker delivery, or guarantee consumer processing.
