# Demo Readiness Checklist

Use this checklist before presenting the repository in a technical interview.

## Before The Demo

- Open `README.md`, `docs/reviewer-entrypoint.md`, and `docs/dead-letter-queue.md`.
- Prepare a producer-consumer message flow explanation.
- Pick one duplicate-delivery or retry scenario.

## During The Demo

- Start with why asynchronous messaging is useful here.
- Show retry, DLQ, replay, and idempotency thinking.
- Explain how message contracts protect producer-consumer independence.

## Strong Closing Point

This project is strongest when presented as failure-aware messaging design:
successful delivery matters, but recovery behavior is the real signal.
