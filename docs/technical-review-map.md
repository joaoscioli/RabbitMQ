# Technical Review Map

Use this map to review the repository quickly without losing the technical story.

## Review Path

1. Start with `docs/technical-elevator-pitch.md` for the short positioning.
2. Read `docs/dead-letter-queue.md` for failure handling.
3. Open `docs/technical-risks.md` for messaging risks and mitigations.
4. Check `docs/acceptance-criteria.md` for the next producer-consumer slice.
5. Finish with `docs/seniority-evidence.md` to connect the project to interview
   signal.

## Reviewer Takeaway

The project should be evaluated as asynchronous architecture evidence: the core
signal is duplicate handling, retries, DLQs, replay, and contract discipline.
