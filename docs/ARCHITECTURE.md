# Architecture

The project uses Java for core enterprise business services and Python for the AI boundary.

## Java microservices
- Validation Service: input validation, Kafka publishing
- Reconciliation Service: source-target comparison

## Python AI service
- retrieval over runbooks
- AI investigation recommendations
- read-only guardrail model

## React frontend
- operational dashboard
- API integration
- AI investigation flow

This split is intentional: Java demonstrates enterprise backend depth while Python demonstrates modern Applied AI capability.
