# Deep Dive Prompts

Use these prompts to move from a high-level walkthrough into a deeper technical
conversation.

## Prompts

- How does the consumer remain safe under duplicate delivery?
- What retry policy avoids overwhelming downstream systems?
- Which failures belong in a DLQ and which should be retried?
- How would replay be controlled after an incident?
- How are message contracts versioned between teams?

## Strong Discussion Direction

The best answers should treat messaging as a distributed-system boundary with
failure, duplicate delivery, backpressure, observability, and recovery concerns.
