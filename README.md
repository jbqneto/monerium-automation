# Monerium DCA API

Java Spring Boot service responsible for integrating with Monerium, receiving deposit-related webhooks, loading the active DCA strategy, and orchestrating execution requests to an external blockchain executor.

This repository contains **only the Java service**.

A separate blockchain execution service is planned for the future, initially expected to be implemented in **Node.js**, but that executor is **not part of this repository**.

## MVP Goal

The initial MVP is focused on a single use case:

1. Receive fiat through a **Monerium IBAN**
2. Route the received value to a **single treasury address on Base**
3. Detect successful deposit processing through **Monerium webhooks**
4. Load the active DCA strategy from the database
5. Generate an investment lot and tranche plan
6. Send an execution request to an **external executor service**
7. Persist execution results for audit and future reconciliation

The MVP targets DCA into:

- BTC
- ETH
- SOL
- BNB
- USDC

## Repository Scope

This repository includes:

- Monerium integration
- Monerium webhook handling
- persistence
- DCA strategy management
- lot and tranche generation
- orchestration toward an external execution service
- execution request/result tracking

This repository does **not** include:

- blockchain execution code
- EVM-specific execution logic
- Solana execution logic
- bridge logic
- swap logic
- Node.js code
- wallet broadcasting logic

## High-Level Architecture

### Java application responsibilities

This service owns:

- Monerium authentication
- Monerium API integration
- linked address management
- IBAN provisioning and retrieval
- webhook subscription management
- webhook signature validation
- order and deposit persistence
- DCA strategy loading
- investment lot and tranche creation
- orchestration of execution requests to an external executor service
- execution result persistence and audit trail

### External executor responsibilities

A future external service is expected to own:

- reading execution instructions from Java
- quoting and routing blockchain operations
- performing swaps and bridges
- interacting with EVM and SVM chains
- transferring final assets to destination addresses
- returning transaction hashes and per-asset execution statuses

The executor is planned, but does not exist yet.

## Boundary Rules

These rules are important and should not be broken:

- **Java owns investment strategy**
- **The external executor owns blockchain execution**
- Java must not contain chain-specific execution logic
- The external executor must not decide portfolio allocation or DCA policy
- Webhook handlers must not execute blockchain operations directly
- DCA execution must start only after Monerium order state is confirmed as `processed`

## Monerium Flow

The intended Monerium flow for the MVP is:

1. A Monerium **sandbox** or **production** app is configured
2. A single treasury wallet address is linked on **Base**
3. A single **IBAN** is requested for that linked address
4. The user sends EUR from a bank account (for example Millennium) to that IBAN
5. Monerium processes the incoming payment and routes value to the linked treasury address
6. Monerium sends webhook events such as:
    - `order.created`
    - `order.updated`
    - `iban.updated`
7. This Java service validates the webhook signature and stores the relevant order/deposit state
8. Once the order is `processed`, the Java service creates an investment lot and prepares execution
9. The Java service sends a request to an external executor
10. The external executor performs the required blockchain operations and responds with results

## Why a Single IBAN in the MVP

The MVP intentionally uses:

- **1 IBAN**
- **1 treasury address**
- **1 entry chain (Base)**

This keeps the first version operationally simple and reduces complexity around:

- reconciliation
- routing logic
- multiple deposit entry points
- multi-IBAN management
- chain selection before strategy execution

Multiple IBANs or more advanced routing may become a future feature.

## Initial DCA Strategy

The initial conservative strategy is:

- BTC: 50%
- ETH: 20%
- SOL: 10%
- BNB: 10%
- USDC: 10% (**Opportunities reserve**)

USDC is intentionally treated as an opportunities reserve. In some execution flows, it may not require conversion and may simply remain as stable liquidity for later manual or automated opportunities.

These values should be stored in the database and loaded as the active strategy during processing.

## Tranche Rules

Current tranche planning rules:

- `amount <= 150 EUR` → execute in **1 tranche**
- `150 < amount <= 500 EUR` → execute in **2 tranches**
- `amount > 500 EUR` → execute in **3 tranches**

Suggested schedule:

- 1 tranche:
    - 100% on day 0

- 2 tranches:
    - 50% on day 0
    - 50% on day 7

- 3 tranches:
    - 40% on day 0
    - 30% on day 7
    - 30% on day 14

These rules may evolve later, but they are the current MVP baseline.

## Tech Stack

### Java service

- Java 21
- Spring Boot 4.x.x
- Spring Web
- Spring Validation
- Spring Data JPA
- PostgreSQL
- Lombok
- Actuator
- Maven

### External execution service

- Separate service
- Planned for future implementation
- Initially expected to be implemented in Node.js
- Communicates with Java over HTTP in the MVP
- May evolve later to queues/events

## Environment Strategy

This project should support at least:

- **dev** → Monerium sandbox
- **prod** → Monerium production

Common configuration lives in `application.yml`, while environment-specific behavior can be controlled via:

- `application-dev.yml`
- `application-prod.yml`

Sensitive values must be provided through environment variables.

## Package Structure

This project follows a **feature-based package structure**.

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

## Core Domain Concepts

### Monerium

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

## Processing Rules

The following processing rules are essential:

- Webhook signatures must be validated before any processing
- `order.created` is useful for traceability, but not enough to trigger DCA
- DCA execution should be triggered only on a valid `order.updated` event with state `processed`
- The Java service may optionally reconcile the order via Monerium API before creating a lot
- Execution requests should be idempotent
- Duplicate webhooks must not create duplicate lots
- Webhook handlers should persist first and execute later

## External Execution Request Model

Java should send one request per tranche to the external executor.

The request should include:

- execution id
- lot id
- tranche id
- source currency
- source network
- source treasury address
- total tranche amount
- target asset allocations
- destination addresses per asset
- metadata for audit and traceability

The external executor should return:

- overall status
- per-asset status
- requested and executed amounts
- chain/network used
- transaction hashes
- bridge hashes when applicable
- error messages when something fails

## Current Development Priorities

Recommended implementation order:

1. Monerium properties/configuration
2. Monerium authentication client
3. authentication context endpoint integration
4. linked address support
5. IBAN request/retrieval support
6. webhook subscription support
7. webhook receiver with signature validation
8. order/deposit persistence
9. DCA strategy persistence
10. lot/tranche generation
11. external execution client contract
12. execution audit trail

## Future Expansion

The MVP is intentionally narrow. Future versions may include:

- real external execution service
- multiple IBANs
- multiple treasury addresses
- more advanced DCA strategies
- user-facing strategy management
- multi-user support
- event-driven async execution
- reconciliation jobs
- retry policies
- monitoring and metrics
- support for additional assets and chains

## Guidance for Codex and Contributors

When working on this project, follow these rules:

- Read this README before proposing structural changes
- Keep the Java/external-executor boundary clear
- Prefer minimal safe steps over broad rewrites
- Do not move blockchain execution into Java
- Do not hardcode strategy values in business logic
- Keep controllers thin
- Keep services focused
- Separate DTOs from entities
- Prefer explicit mappers
- Avoid overengineering
- Avoid introducing hexagonal architecture unless explicitly requested
- Keep names, classes, methods, DTOs, and comments in English

## Project Status

This project is currently in the initial architecture and integration setup phase.

The immediate goal is to establish a reliable Monerium integration pipeline that can:

- receive fiat through IBAN
- confirm processed orders
- create DCA lots
- prepare integration with a future dedicated blockchain execution service