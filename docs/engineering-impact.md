# Engineering Impact

This file explains the engineering impact demonstrated by the project.

## Impact Areas

| Area | Impact |
| --- | --- |
| Decoupling | Producers and consumers can evolve around explicit contracts. |
| Failure recovery | Retry, DLQ, replay, and idempotency reduce hidden message loss. |
| Operations | Queue depth, retry volume, and DLQ growth become reviewable signals. |
| System design | Messaging complexity is justified only when it solves a real workflow need. |

## Interview Point

The strongest signal is explaining reliability around RabbitMQ instead of
claiming the broker makes the whole system reliable by itself.
