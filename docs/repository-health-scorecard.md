# Repository Health Scorecard

Use this scorecard to review the project before sharing it in an interview.

| Area | Current Signal | Next Upgrade |
| --- | --- | --- |
| README | Messaging purpose and operational topics are visible. | Add a runnable producer-consumer demo path near the top. |
| Architecture | Retry, DLQ, backpressure, and idempotency are documented. | Add persistence-backed idempotency example. |
| Tests | Message testing strategy is mapped. | Add contract and consumer failure tests. |
| CI/CD | Repository hygiene and automation are present. | Add workflow checks for sample code when expanded. |
| Interview Readiness | Strong failure-handling discussion material exists. | Add a sequence diagram for a failed message flow. |

## Health Rule

A portfolio repository is healthy when a reviewer can understand purpose,
trade-offs, verification, and next steps without guessing.
