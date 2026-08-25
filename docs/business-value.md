# Business Value

This project demonstrates how messaging supports resilient backend workflows.

## Why It Matters

- Queues can decouple slow or failure-prone work from synchronous requests.
- Retry and dead-letter flows make failures visible and recoverable.
- Message contracts help teams integrate services without hidden assumptions.

## Portfolio Signal

The value is not only sending messages. The signal is showing how asynchronous
architecture handles failure, duplicate delivery, backpressure, and operational
visibility.

## Interview Talking Point

I would use this project to discuss when messaging is worth the added complexity
and how to design reliability around RabbitMQ primitives.
