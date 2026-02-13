# DBM-SI Structural Messaging: Payload-Structure Communication Model

**Version:** v0.1  
**Scope:** Conceptual Specification + Example Message Package  
**Keywords:** Structural Intelligence, Evidence Chain, Auditability, Reproducibility, Runtime Messaging

---

## Canonical Definition (v1.0)

Structural Messaging in DBM-SI defines the communication model through which runtime payload and structural evidence are jointly transmitted, validated, and frozen into reproducible system states. Unlike conventional messaging systems that transport data alone, Structural Messaging carries payload together with structural context — including evidence chains, invariant hashes, execution status, and snapshot references — ensuring that structural intelligence execution remains auditable, reproducible, and verifiable across runtime boundaries. This model serves as the bridge between execution payload flow and structural knowledge formation in Structural Intelligence systems.

---

## What This DOI Package Contains

- **`StructuralMessaging-Spec.md`** — the conceptual specification (v0.1)
- **`diagrams/messaging-stack.mmd`** — the Messaging Stack diagram (Mermaid)
- **`examples/*.json`** — minimal example messages:
  - EvidenceMessage
  - ExecutionReceipt
  - SnapshotMessage
  - ConvergenceReport

This package is intended to be:
- a stable reference for the DBM-SI runtime communication abstraction
- a reproducibility- and audit-oriented message model
- a medium-independent communication concept for Structural Intelligence systems

---

## Core Model

Structural Messaging treats a message as:

> **StructuralMessage = Payload + StructuralContext**

Where **StructuralContext** includes evidence-chain identifiers, invariant hashes, validator outputs, execution status, snapshot references, and convergence signals.

---

## Messaging Objects

### EvidenceMessage
Minimal transferable unit carrying:
- payload
- evidenceHash / invariantHash
- producer metadata

### ExecutionReceipt
Verifiable record capturing:
- status (OK/SAFE_MODE/STOPPED/COOLDOWN/QUARANTINE)
- evidenceChainHead and snapshot linkage
- cost / mode / reason for audit

### SnapshotMessage
Frozen structural knowledge state capturing:
- baselineId, eventHashes
- validatorState and convergenceState
- reproducibility-critical references

---

## State Model

ExecutionStatus values:
- OK
- SAFE_MODE
- STOPPED
- COOLDOWN
- QUARANTINE

Status MUST be propagated via receipts and snapshots to ensure validator-independent observability.

---

## Propagation Horizon (Medium Independence)

Structural Messaging is not bound to a transport medium; it is bound to **structural state synchronization**.

Classical media (memory/storage/network) are supported by default. A quantum entanglement analogy is included as a theoretical illustration of state correlation beyond classical payload transfer; it is not an implementation claim.

---

## How To Cite

Use this DOI as the reference for the DBM-SI structural messaging model and its example message formats.

---

## License

Choose a license consistent with the parent DBM-SI/DBM-COT release policy (e.g., CC BY 4.0 for specs/docs, or Apache-2.0 for reference code if later added).
