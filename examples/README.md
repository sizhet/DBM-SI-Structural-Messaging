##How to Run the Demo

The Structural Messaging demo can be executed using Maven.

Run unit tests:

    mvn test


Run the ITEM #266 demo:

    mvn exec:java -Dexec.mainClass="com.dbm.structure_messaging.demo.Item266Demo"


If the exec plugin is not configured, run directly:

    java -cp target/classes com.dbm.structure_messaging.demo.Item266Demo

##What to Look For in the Output

The demo illustrates the Structural Messaging cycle described in ITEM #266.

Key runtime signals to observe:

##HeadCode / WordToken

Represents the constrained payload encoding mechanism.

This demonstrates how fragmented payload information can be transmitted through structured tokens.

##Training Table Construction

Shows how shared encoding conventions are built between sender and receiver.

This corresponds to the “shared structural encoding” concept in ITEM #266.

##Decoder / Reconstruction

Demonstrates how payload fragments are reconstructed from structured observations.

This reflects the Fragment Resonance Principle.

##Vote Convergence

Demonstrates structural convergence behavior across candidate decoding paths.

This connects to:

    ConvergenceChecker
    Snapshot stability
    Structural invariants

##Expected Demo Summary (Conceptual)

A typical run demonstrates:

    -- payload encoding via structured tokens
    -- decoder reconstruction from fragments
    -- vote-based convergence stabilization

The demo intentionally remains minimal to highlight the payload-structure communication logic rather than application-specific constraints.

##Demo Output Summary Line

For easier reading, the demo prints a compact summary line describing:

    -- encoded payload tokens
    -- best decoded candidate
    -- convergence status

This helps readers quickly understand the execution cycle without inspecting full logs.

##Structural Messaging Example Files

This directory contains example Structural Messaging objects used by the specification and ITEM #266.

These files illustrate how runtime payload and structural context are represented in practice.

##File Mapping


|File	|Structural Object	|Runtime Stage |
| -------- | -------- | -------- |
|evidence-message.json	|EvidenceMessage	|After algorithm execution|
|receipt.json	|ExecutionReceipt	|After runtime execution|
|snapshot.json	|SnapshotMessage	|After state freezing|
|convergence-report.json	|ConvergenceReport	|After convergence evaluation|

##Relationship to Specification

These examples correspond to the object definitions in:

    StructuralMessaging-Spec.md


They demonstrate conceptual field structures such as:

    -- evidenceHash
    -- invariantHash
    -- ExecutionStatus
    -- snapshotId
    -- convergenceState

The examples are illustrative rather than tied to a specific runtime implementation.

##Relationship to ITEM #266 Demo

The demo program in:

    src/main/java/com/dbm/structure_messaging/demo/


produces runtime states conceptually equivalent to the objects shown here.

The examples help connect:

    Architecture explanation (ITEM #266)
    Protocol definition (Spec)
    Runtime behavior (Demo)

##Purpose of These Examples

The goal of these files is to:

    -- illustrate Structural Messaging object structure
    -- support reproducibility and documentation
    -- provide reference payload+structure message formats

They are intentionally minimal.

Structural Messaging is defined by structural context propagation, not by any specific payload schema.