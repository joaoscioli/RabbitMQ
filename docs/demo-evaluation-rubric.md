# Demo Evaluation Rubric

Use this rubric to evaluate a technical walkthrough of this project.

| Area | Strong Signal | Weak Signal |
| --- | --- | --- |
| Messaging model | Explains producers, exchanges, queues, and consumers. | Says messages make everything reliable. |
| Failure handling | Covers acknowledgements, retries, DLQs, and replay. | Retries forever or ignores poison messages. |
| Idempotency | Designs for duplicate delivery. | Assumes exactly-once processing. |
| Observability | Names queue depth, retry volume, and DLQ signals. | Has no way to diagnose broker behavior. |

## Passing Bar

A strong demo explains where RabbitMQ helps and where system design must still
handle reliability.
