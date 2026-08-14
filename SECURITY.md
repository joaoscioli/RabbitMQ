# Security Policy

This repository is a RabbitMQ messaging lab, so security feedback should focus
on local configuration, message data handling, dependencies, and operational
guidance.

## Supported Scope

Security feedback is welcome for:

- unsafe RabbitMQ defaults in examples;
- accidental secret exposure;
- dependency vulnerabilities;
- message payload examples that include sensitive data;
- misleading operational guidance around retries, DLQs, or observability.

## Reporting

Please do not open a public issue with sensitive details.

Send a private report to `joaoscioli@outlook.com` with the affected file, the
risk, and a suggested mitigation when possible.

## Security Expectations

- Local credentials must stay example-only.
- Message examples should avoid sensitive personal or business data.
- Observability examples should avoid logging full sensitive payloads.
