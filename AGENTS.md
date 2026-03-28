# AGENTS.md

## Project purpose

This repository contains the Java Spring Boot service responsible for the Monerium side of a DCA system.

The MVP goal is:

1. Receive fiat through a Monerium IBAN
2. Route funds to a single treasury wallet on Base
3. Detect processed incoming funds through Monerium webhooks
4. Load the active DCA strategy from persistence
5. Generate investment lots and tranches
6. Send execution requests to an external blockchain executor
7. Persist execution results for audit and reconciliation

This repository contains **only the Java service**.

A separate external blockchain executor is planned for the future, initially expected to be implemented in Node.js, but that executor is **not part of this repository**.

---

## System boundary

### Java service owns

- Monerium authentication and API integration
- linked address management
- IBAN provisioning and retrieval
- webhook subscription management
- webhook signature validation
- order and deposit persistence
- DCA strategy ownership
- lot and tranche creation
- orchestration to the external executor
- execution request/result persistence
- audit trail and reconciliation support

### External executor owns

- chain-specific execution
- quoting
- swaps
- bridges
- wallet operations
- broadcasting transactions
- final delivery of assets to destination addresses
- returning transaction hashes and execution status

### Boundary rules

These rules must be preserved:

- Java owns investment strategy
- The external executor owns blockchain execution
- Do not move on-chain execution logic into Java
- Do not move portfolio allocation logic into the external executor
- Webhook handlers must not execute blockchain operations directly
- DCA execution starts only after a Monerium order is confirmed as `processed`

---

## MVP assumptions

Current MVP assumptions:

- single Monerium IBAN
- single treasury wallet
- single entry chain: Base
- single active DCA strategy
- single user / personal automation flow
- Java to external executor communication over HTTP
- no queue is required in the first version
- no multi-user support in the MVP
- the external executor may not exist yet

Do not expand the architecture unless explicitly requested.

---

## DCA strategy

Current conservative default strategy:

- BTC = 50
- ETH = 20
- SOL = 10
- BNB = 10
- USDC = 10

USDC is the opportunities reserve and may not require swap execution depending on the source asset and destination strategy.

These values should not be hardcoded deep inside business logic.

Preferred approach:

- store strategy in the database
- mark one strategy as active
- let Java load the active strategy at processing time

---

## Tranche rules

Current tranche rules:

- `amount <= 150 EUR` -> 1 tranche
- `150 < amount <= 500 EUR` -> 2 tranches
- `amount > 500 EUR` -> 3 tranches

Suggested scheduling:

- 1 tranche:
  - 100% on day 0

- 2 tranches:
  - 50% on day 0
  - 50% on day 7

- 3 tranches:
  - 40% on day 0
  - 30% on day 7
  - 30% on day 14

When implementing money calculations:

- use `BigDecimal`
- define rounding explicitly
- let the last allocation absorb rounding differences when needed

---

## Implementation priorities

Prefer this implementation order unless explicitly told otherwise:

1. configuration properties
2. Monerium auth client/service
3. auth context integration
4. linked address support
5. IBAN request/retrieval support
6. webhook subscription support
7. webhook receiver and signature validation
8. order/deposit persistence
9. strategy persistence
10. lot/tranche generation
11. external execution client contract
12. execution result persistence
13. scheduler/reconciliation support

Do not jump early into advanced abstractions.

---

## Architecture style

Use a **feature-based package structure**.

Preferred package layout:

```text
com.jbqneto.moneriumdca
├── config
├── shared
│   ├── api
│   ├── enums
│   ├── exception
│   ├── util
│   └── validation
├── monerium
│   ├── client
│   ├── config
│   ├── controller
│   ├── dto
│   │   ├── request
│   │   └── response
│   ├── entity
│   ├── mapper
│   ├── repository
│   ├── service
│   └── webhook
├── investment
│   ├── controller
│   ├── dto
│   │   ├── request
│   │   └── response
│   ├── entity
│   ├── mapper
│   ├── repository
│   ├── service
│   └── strategy
├── execution
│   ├── client
│   ├── controller
│   ├── dto
│   │   ├── request
│   │   └── response
│   ├── entity
│   ├── mapper
│   ├── repository
│   └── service
└── scheduler
```

Do not reorganize into hexagonal / ports-and-adapters unless explicitly requested.

---

## Coding conventions

### Language

- Write all code in English
- Use English for:
  - class names
  - method names
  - variable names
  - DTO names
  - comments
  - enums
  - database-facing names when applicable

### General style

- Prefer small, focused services
- Keep controllers thin
- Keep business logic out of controllers
- Separate DTOs from entities
- Prefer explicit mappers
- Prefer composition over large god services
- Avoid premature abstraction
- Avoid speculative generic frameworks

### Java specifics

- Use Java 21 unless explicitly changed by the user
- Use Spring Boot 4.x.x
- Use `BigDecimal` for money
- Prefer constructor injection
- Avoid field injection
- Use `@ConfigurationProperties` for typed config
- Keep external API clients isolated in `client`
- Keep persistence concerns in `repository`
- Keep orchestration in dedicated services
- Design the external execution integration behind a client boundary

### Persistence

- Prefer clear entities over clever inheritance
- Keep entity relationships simple in the MVP
- Be careful with lazy loading
- Avoid leaking entities directly through API responses
- Use database as the source of truth for strategy, orders, lots, tranches, and execution results

---

## Webhook handling rules

Webhook handling is critical.

Always follow these rules:

- validate signature before processing
- use the raw request body for signature validation
- persist first, then process
- make processing idempotent
- tolerate duplicate webhook delivery
- do not trigger blockchain execution directly inside the HTTP webhook handler
- hand off to service/scheduler/orchestrator after persistence

### Trigger rule

- `order.created` is useful for traceability
- `order.updated` with state `processed` is the valid trigger for DCA
- `order.updated` with state `rejected` should mark failure and stop execution

---

## Idempotency expectations

This project must behave safely under retries and repeated external events.

Design for idempotency in:

- webhook ingestion
- order persistence
- lot creation
- tranche creation
- execution request creation
- external execution calls where possible

Typical examples:

- same webhook should not create multiple lots
- same processed order should not trigger duplicate execution
- execution retries should be traceable and controlled

---

## Communication with the external executor

Java sends one execution request per tranche.

Java should define:

- what amount is available
- which strategy applies
- the asset allocations
- the source address/network
- destination addresses per asset
- execution identifiers for traceability

The external executor should define:

- how each asset is acquired
- which route is used
- whether bridge/swap is needed
- final transaction details

Do not blur this boundary.

USDC should be treated as an opportunities reserve. Depending on the flow, it may remain as USDC without requiring a swap.

If the external executor does not exist yet, prefer:

- explicit DTO contracts
- an execution client interface or focused client service
- a fake/mock implementation for local progress
- persistence of outbound execution requests for future integration

Do not generate Node.js code in this repository unless explicitly requested.

---

## What to avoid

Avoid the following unless explicitly requested:

- Kafka
- RabbitMQ
- Redis
- event sourcing
- multi-tenant design
- multi-user design
- CQRS split
- advanced hexagonal architecture
- generic internal frameworks
- overuse of interfaces without clear need
- premature plugin systems
- strategy engines more complex than necessary
- direct blockchain execution inside Java

Keep the MVP lean.

---

## Safe behavior for Codex

When proposing or applying changes:

- prefer minimal safe next steps
- explain tradeoffs briefly when relevant
- do not rewrite unrelated parts of the codebase
- do not introduce large refactors without explicit request
- preserve current architecture decisions
- ask for clarification only when truly necessary
- if a task can be done incrementally, do the smallest meaningful increment first

When generating code:

- generate compilable code
- keep naming consistent
- keep package placement coherent
- avoid placeholder-heavy code unless the user explicitly asked only for skeletons
- keep the repository scoped to Java only

---

## Preferred first deliverables

If the codebase is still being initialized, the preferred first concrete deliverables are:

1. `MoneriumProperties`
2. `ExecutorProperties`
3. `DcaProperties`
4. `ConfigurationProperties` wiring
5. HTTP client configuration
6. Monerium auth client
7. Monerium auth service
8. basic Monerium controller for health/test integration
9. linked address module
10. IBAN module
11. webhook subscription module
12. webhook receiver module
13. external execution DTO contract
14. fake external execution client

---

## Domain concepts

The following concepts are expected to exist in the Java service.

### Monerium

Documentation: https://monerium.dev/

- `MoneriumDeposit`
- `MoneriumOrder`
- `MoneriumWebhookEvent`

### Investment

- `InvestmentStrategy`
- `InvestmentLot`
- `InvestmentTranche`
- `TrancheAllocation`

### Execution

- `ExecutionRequest`
- `AssetExecutionResult`

Names may evolve slightly, but these concepts should remain clear.

---

## Final instruction

When in doubt:

- preserve the Java/external-executor boundary
- keep the MVP simple
- optimize for correctness, traceability, and clean layering
- prefer explicitness over cleverness