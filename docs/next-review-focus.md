# Next Review Focus

This file defines the next focused improvement for a technical reviewer to watch.

## Current Focus

Add a consumer example with explicit acknowledgement and duplicate handling.

## Why It Matters

The repository already documents reliability concepts. A focused consumer
example would make those concepts concrete and show how idempotency changes
message-processing code.

## Expected Evidence

- A small consumer flow with manual acknowledgement.
- Duplicate-message handling documented through a test or scenario.
- Notes explaining how DLQ behavior protects the system from poison messages.
