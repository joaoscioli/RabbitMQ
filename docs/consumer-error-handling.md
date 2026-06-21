# Consumer Error Handling

Message consumers need explicit failure behavior.

## Goal

A consumer should make a clear decision when message processing fails:

- retry later;
- reject permanently;
- send to a dead-letter queue;
- alert an operator;
- ignore only when the message is safely irrelevant.

## Common Failure Types

### Transient Failure

Temporary problems such as downstream timeout or database connection failure.

Recommended behavior:

- retry with limits;
- avoid infinite loops;
- preserve the original message.

### Permanent Failure

Problems such as invalid schema, missing required fields, or unsupported event
type.

Recommended behavior:

- reject or dead-letter;
- log the reason;
- do not retry forever.

### Poison Message

A message that repeatedly fails and blocks useful work.

Recommended behavior:

- isolate in DLQ;
- include correlation id and failure reason;
- investigate before replaying.

## Portfolio Signal

Error handling is where asynchronous systems become real. Publishing and
consuming happy-path messages is useful, but production systems need failure
paths that are visible and controlled.
