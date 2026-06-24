# Security Policy

This repository is a RabbitMQ messaging lab. Security review focuses on safe
configuration, message handling, and avoiding leaked credentials.

## Supported Scope

Security review focuses on:

- RabbitMQ credentials in local configuration;
- unsafe queue or exchange examples;
- message payloads that include sensitive data;
- replay behavior that could duplicate sensitive operations;
- dependency risk in the Spring Boot example.

## Reporting A Security Concern

Open a concise issue describing the affected flow, configuration, or document.
Do not include real broker credentials or private payloads.

## Development Practices

- Keep broker credentials out of Git.
- Use synthetic message payloads.
- Document security assumptions for replay, retries, and dead-letter flows.
