# Reviewer Entrypoint

Use this page when you have only a few minutes to evaluate the repository.

## First Three Files

- `README.md` for the project purpose and navigation.
- `docs/technical-scope.md` for what the project proves today.
- `docs/dead-letter-queue.md` for failure handling.

## What To Look For

- Messaging decisions connected to reliability and operational recovery.
- Explicit handling for retries, idempotency, DLQs, replay, and backpressure.
- Message contracts that keep producers and consumers independently maintainable.

## Strong Interview Signal

The repository is strongest when discussed as an asynchronous architecture lab:
it shows how to reason about failure before scaling the messaging topology.
