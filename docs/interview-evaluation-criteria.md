# Interview Evaluation Criteria

Use these criteria to evaluate the project during a technical interview.

## Baseline Signal

- Explains producer, consumer, exchange, queue, and routing concepts.
- Describes why asynchronous messaging can be useful.
- Navigates retry and DLQ documentation.

## Strong Signal

- Discusses duplicate delivery, idempotency, and bounded retries.
- Explains when failures should go to a dead-letter queue.
- Connects message contracts to producer-consumer independence.

## Senior Signal

- Designs for recovery before scale.
- Balances async benefits against operational complexity.
- Treats messaging as a distributed-system boundary with failure, replay, and versioning concerns.
