# Technical Debt Register

This register makes project trade-offs visible instead of hiding unfinished work.

## Current Debt

| Area | Debt | Impact | Next Action |
| --- | --- | --- | --- |
| Runnable Examples | Some messaging flows are explained before full execution. | Reviewers may want to run consumers locally. | Add a Docker Compose lab with producer and consumer. |
| Observability | Metrics are documented before complete dashboards. | Failure diagnosis is less demonstrable. | Add queue depth, retry, and DLQ metrics examples. |
| Idempotency | Strategy is documented before persistence-backed proof. | Duplicate-message handling is not fully verifiable. | Add an idempotency store example. |

## Review Rule

Debt is acceptable when it is explicit, bounded, and connected to a follow-up
decision. Hidden debt is what damages engineering trust.
