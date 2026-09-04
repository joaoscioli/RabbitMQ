# Technical Storytelling Guide

Use this guide to present the project clearly in an interview.

## Story Arc

1. Start with the problem: some workflows need decoupling, buffering, and
   failure handling beyond synchronous requests.
2. Explain the messaging boundary: producers, contracts, exchanges, queues,
   consumers, acknowledgements, retries, and DLQs.
3. Defend reliability: RabbitMQ gives primitives, but the system must handle
   duplicates, poison messages, replay, and backpressure.
4. Close with operations: queue depth, retry volume, DLQ growth, and latency
   must be observable.

## Strong Closing

This project is designed to show practical reasoning about asynchronous
architecture and operational failure modes.
