# Production Readiness Matrix

This matrix separates messaging examples from production messaging readiness.

| Capability | Current State | Production Expectation |
| --- | --- | --- |
| Message contracts | Templates and boundaries are documented. | Versioned schemas and compatibility checks. |
| Failure handling | Retry, DLQ, and idempotency are mapped. | Tested poison-message and replay procedures. |
| Observability | Key broker signals are documented. | Dashboards and alerts for queue depth, retries, and DLQs. |
| Operations | Local setup and topology are described. | Capacity planning, backup, and incident runbooks. |

## Review Note

The repo is strongest when it explains that RabbitMQ provides primitives, while
reliability comes from surrounding system design.
