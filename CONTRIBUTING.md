# Contributing

This repository is maintained as a RabbitMQ messaging lab.

## Working Style

- Add one messaging concept or flow at a time.
- Keep examples small and explain the business reason behind the message flow.
- Document reliability behavior: retries, dead letters, idempotency, and replay.
- Update local setup notes when infrastructure changes.

## Before Opening A Pull Request

- Run the test suite when the Java project changes.
- Check whether the message contract or topology documentation needs an update.
- Keep broker assumptions explicit.

## Review Focus

Reviewers should look for reliable consumer behavior, clear topology naming,
safe failure handling, and operational visibility.
