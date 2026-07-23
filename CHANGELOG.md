# Changelog

This project follows small, reviewable increments. Dates use `YYYY-MM-DD`.

Review cadence: each portfolio update should improve message reliability,
contract clarity, operational visibility, or failure handling.

## How To Discuss This History

Use this changelog to show messaging maturity. The strongest narrative is moving
from simple send/consume examples to routing, contracts, idempotency, and
producer-side failure visibility.

## Unreleased

- Planned: publisher return handling for unroutable messages.
- Planned: explicit domain event example with routing-key ownership.

## 2026-07 Portfolio Hardening

- Added mandatory publishing configuration.
- Improved context test isolation from a local broker.
- Expanded messaging documentation around contracts, producer safeguards, and
  reliability.
