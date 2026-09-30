# Implementation Readiness

This file defines what must be clear before adding the next messaging lab.

## Ready When

- The message contract is stable enough for producer and consumer examples.
- Exchange, queue, and routing choices are documented.
- Retry, DLQ, and replay behavior are defined.
- Idempotency expectations are clear for the consumer.
- Failure scenarios are part of the demo.

## Not Ready If

- The flow only demonstrates successful delivery.
- Duplicate delivery is ignored.
- Replay behavior would require manual guessing.

## Review Focus

Implementation should begin with a small producer-consumer lab that proves
failure-aware messaging behavior.
