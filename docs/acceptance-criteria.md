# Acceptance Criteria

This file defines what the next implementation slice must satisfy before it is
considered ready for review.

## Producer And Consumer Lab

- A producer publishes a documented message contract.
- A consumer processes successful messages and acknowledges them.
- Failed messages follow a bounded retry path.
- Exhausted failures are routed to a dead-letter queue.
- Replay and idempotency notes are documented.

## Review Standard

The slice is ready when a reviewer can reason about success, failure, duplicate
delivery, and recovery without guessing how the messaging flow behaves.
