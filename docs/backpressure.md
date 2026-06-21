# Backpressure

Backpressure happens when producers publish messages faster than consumers can
process them.

## Why It Matters

Without backpressure awareness, queues can grow until the system becomes slow,
expensive, or unstable.

## Warning Signs

- queue depth grows continuously;
- consumer latency increases;
- retry queues grow faster than they drain;
- dead-letter volume increases;
- downstream services become saturated.

## Design Responses

- scale consumers horizontally;
- reduce producer rate;
- batch work carefully;
- add retry limits;
- protect downstream dependencies with timeouts;
- alert on queue depth and processing latency.

## What Not To Do

- Do not add unlimited retries.
- Do not ignore growing queues because producers are still succeeding.
- Do not increase consumer concurrency without checking downstream capacity.

## Portfolio Signal

Backpressure shows that messaging knowledge includes operations and capacity,
not only exchanges, queues, and routing keys.
