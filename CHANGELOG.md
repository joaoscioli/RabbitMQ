# Changelog

This project follows small, reviewable increments. Dates use `YYYY-MM-DD`.

Review cadence: each portfolio update should improve message reliability,
contract clarity, operational visibility, or failure handling.

## How To Discuss This History

Use this changelog to show messaging maturity. The strongest narrative is moving
from simple send/consume examples to routing, contracts, idempotency, and
producer-side failure visibility.

## Review Readiness

Ready for interview review when messaging examples discuss routing, retries,
DLQs, idempotency, and producer failure visibility together.

Current public signal: RabbitMQ documentation and Spring Boot examples focused
on reliable messaging instead of basic queue usage only.

## Unreleased

- Planned: publisher return handling for unroutable messages.
- Planned: explicit domain event example with routing-key ownership.

## 2026-08 Interview Readiness

- Added fast review path, interview case study, and stronger messaging talking
  points for technical interviews.
- Refined GitHub hygiene with issue, pull request, CODEOWNERS, security, support,
  contributing, and Dependabot configuration updates.
- Clarified how to present reliability through retries, DLQs, idempotency,
  replay, and producer-side failure visibility.

## 2026-07 Portfolio Hardening

- Added mandatory publishing configuration.
- Improved context test isolation from a local broker.
- Expanded messaging documentation around contracts, producer safeguards, and
  reliability.
