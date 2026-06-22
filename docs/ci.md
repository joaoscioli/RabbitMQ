# CI Workflow

This repository uses GitHub Actions to run the Spring Boot RabbitMQ lab tests on
pushes to `main` and on pull requests.

## What The Workflow Checks

- The Maven project can be built in a clean Linux runner.
- Java 11 is available, matching the current Spring Boot 2.x project baseline.
- The test suite can run without depending on a developer machine.
- The Maven wrapper is executable in CI.

## Why This Matters

Messaging examples can become fragile when they rely on local configuration or
manually started services. A CI workflow gives reviewers a fast signal that the
project still has a runnable baseline.

For portfolio review, this shows that the repository is not only a collection
of notes. It is being shaped as a maintained engineering lab with automated
feedback.

## Future Improvements

- Add integration tests with RabbitMQ through Testcontainers.
- Publish test reports as workflow artifacts when failures occur.
- Add static analysis once the example code is reorganized.
