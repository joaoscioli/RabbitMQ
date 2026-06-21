# Message Replay

Message replay is the process of reprocessing messages after a failure has been
understood and corrected.

## When Replay Is Useful

- a downstream dependency was temporarily unavailable;
- a consumer bug was fixed;
- a configuration issue caused messages to fail;
- a dead-letter queue contains valid messages that can now be processed.

## Replay Risks

Replay can be dangerous when consumers are not idempotent.

Risks include:

- duplicate emails;
- duplicate payments;
- duplicated audit entries;
- inconsistent subscription state.

## Recommended Practice

- Inspect the dead-letter queue before replaying.
- Confirm the original failure is fixed.
- Replay in small batches.
- Monitor consumer logs and queue depth.
- Use idempotency keys for business operations.
- Keep an audit trail of replay actions.

## Portfolio Signal

Replay strategy shows operational maturity. It proves the engineer understands
that message-driven systems need recovery workflows, not only publish and
consume examples.
