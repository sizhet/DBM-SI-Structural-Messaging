# Release Notes — v0.1.1 (2026-02-16)
DBM-SI Structural Messaging: Payload-Structure Communication Model

---

## Highlights

This release stabilizes the documentation stack and improves cross-document alignment for the Structural Messaging DOI package.

Structural Messaging defines the payload-structure communication model for Structural Intelligence runtime systems, enabling execution auditability, validator visibility, and snapshot-level reproducibility.

---

## What’s Included

### Documentation Stack (Three-Layer)

- **Architecture Layer:** DBM-COT ITEM #266 (bilingual) — explanation, encoding principles, runtime flow, constrained communication narrative.
- **Protocol Layer:** StructuralMessaging-Spec v0.1.1 — object model + state model + propagation model.
- **Instance Layer:** `examples/*.json` — evidence-message / receipt / snapshot / convergence-report (+ `examples/README.md` mapping).

### Visual Assets

- GitHub header banner (PNG)
- Spec cover PDFs (standard + DBM-SI octagon + DBM-SI color palette)

### Reference Implementation

- Minimal Java demo (Item266Demo / orchestrator and model)
- Unit tests for reproducibility and convergence behavior

---

## Changes Since v0.1

### Spec

- StructuralMessaging-Spec upgraded to v0.1.1 with alignment notes:
  - Adds explicit reference to ITEM #266 for architectural motivation
  - Adds constrained-communication origin note
  - No protocol changes to objects/state model

### Architecture Document (ITEM #266)

- Expanded explanation for constrained payload signaling:
  - Bridge signaling as primary onboarding example
  - Trigger-based observation principle
  - Quantum-entanglement analogy as extreme-limit illustration (no implementation claim)

### Repository Readability

- README updated:
  - How to Run + What to Look For
  - Demo output summary guidance
  - Acknowledgement section added
- `examples/README.md` added to map JSON instances to Spec objects and runtime stages

---

## Compatibility / Breaking Changes

- No breaking changes to the conceptual protocol model.
- Reference demo remains minimal by design; application constraints are intentionally not generalized into a universal SDK.

---

## Verification

- `mvn test` passes (unit tests green).
- Demo runnable under Maven build.

---

## Known Limitations

- License selection may be refined depending on final repository policy (docs vs code).
- Quantum entanglement remains a theoretical analogy only.

---

## Next Steps (Optional)

- Add JSON Schemas under `schemas/` for strong typing (without expanding scope).
- Provide a merged “Spec Cover + Spec Body” publication PDF.
- Add Zenodo/DOI citation block once DOI is minted.
