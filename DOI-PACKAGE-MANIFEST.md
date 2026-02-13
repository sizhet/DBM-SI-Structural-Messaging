# DOI Package Manifest — DBM-SI Structural Messaging
## Zenodo Upload Checklist (v0.1.x)

Date: 2026-02-14
Repository: DBM-SI-Structural-Messaging

This manifest lists the files recommended for inclusion in the Zenodo DOI upload.
Goal: ensure the DOI package is reproducible, readable, and minimal.

---

# 1) REQUIRED (Core DOI Payload)

## 1.1 Top-level documents
- README.md
- StructuralMessaging-Spec.md  (v0.1.1)

## 1.2 Architecture document (DBM-COT ITEM)
- DBM-COT/items/ITEM-266-Structural-Messaging-Architecture.md
- DBM-COT/items/ITEM-266-Structural-Messaging-Architecture-CN.md  (if bilingual release)
- DBM-COT/items/ITEM-266-FIGURES.pdf

## 1.3 Diagrams
- diagrams/messaging-stack.mmd
- (Optional) any additional diagrams used by ITEM #266 or README

## 1.4 Examples (instance layer)
- examples/README.md
- examples/evidence-message.json
- examples/receipt.json
- examples/snapshot.json
- examples/convergence-report.json

---

# 2) RECOMMENDED (Reference Implementation)

Include the minimal Java demo and tests to support reproducibility.

## 2.1 Build metadata
- pom.xml

## 2.2 Source code (reference implementation)
- src/main/java/**

## 2.3 Unit tests (reproducibility proof)
- src/test/java/**

---

# 3) OPTIONAL (Publication Assets)

These are helpful for “publication feel” but not required for the protocol itself.

- StructuralMessaging-Spec-v0.1.1-Cover.pdf
- StructuralMessaging-Spec-v0.1.1-Octagon-Cover-DBMSI-Colors.pdf
- GitHub header banner PNG (if you want the DOI package to be visually complete)

---

# 4) EXCLUDE (Do Not Upload)

These files are not part of the scholarly DOI payload and should not be uploaded.

## 4.1 Build artifacts
- target/**
- *.class

## 4.2 IDE / local workspace
- *.iml
- .idea/**
- .vscode/**

## 4.3 Local helper scripts (optional; include only if needed for reproducibility)
- change_all.sh
- files_listing.txt
- src/allFiles.txt

## 4.4 Private / environment-specific
- any local absolute paths
- any private view notes
- credentials / tokens / secrets

---

# 5) Final Pre-Upload Checks

- [ ] `mvn test` passes on a clean machine
- [ ] README contains “How to Run” and “What to Look For”
- [ ] Spec version matches repository tag (v0.1.1)
- [ ] ITEM #266 EN/CN and Spec cross-references are consistent
- [ ] examples/README.md present and accurate
- [ ] License file added (even if placeholder)
- [ ] CITATION metadata added (CITATION.cff or CITATION.md) if desired

---

# End of Manifest
