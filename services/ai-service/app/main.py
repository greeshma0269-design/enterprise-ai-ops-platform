import re
from typing import Literal
from fastapi import FastAPI
from pydantic import BaseModel, Field

app = FastAPI(title="AI Investigation Service", version="0.1.0")

RUNBOOKS = [
    "Pricing validation: non-positive prices often indicate upstream mapping or malformed source data.",
    "Duplicate ingestion: duplicate IDs can result from retries or missing idempotency keys.",
    "Reconciliation mismatch: compare transformation rules, event lag and downstream consumer failures."
]

class ExceptionItem(BaseModel):
    record_id: str
    rule_code: str
    field: str
    message: str

class InvestigationRequest(BaseModel):
    batch_id: str
    source: str
    exceptions: list[ExceptionItem] = Field(default_factory=list)

class InvestigationResponse(BaseModel):
    batch_id: str
    likely_cause: str
    evidence: list[str]
    recommended_actions: list[str]
    confidence: Literal["low","medium","high"]
    retrieved_context: list[str]
    limitations: list[str]

def tokens(text: str):
    return set(re.findall(r"[a-zA-Z0-9_]+", text.lower()))

def retrieve(query: str):
    q = tokens(query)
    ranked = sorted(((len(q & tokens(d)), d) for d in RUNBOOKS), reverse=True)
    return [d for score, d in ranked[:3] if score > 0]

@app.get("/health")
def health():
    return {"status":"ok"}

@app.post("/api/v1/investigate", response_model=InvestigationResponse)
def investigate(request: InvestigationRequest):
    text = " ".join(f"{e.rule_code} {e.field} {e.message}" for e in request.exceptions)
    context = retrieve(text)
    codes = {e.rule_code for e in request.exceptions}

    if "NON_POSITIVE_PRICE" in codes:
        cause = "Invalid pricing suggests an upstream mapping or source-data quality issue."
        actions = ["Inspect raw source data.","Confirm currency/decimal mapping.","Replay corrected records only."]
        confidence = "high"
    elif "DUPLICATE_RECORD" in codes:
        cause = "Duplicate IDs suggest repeated ingestion or an idempotency gap."
        actions = ["Compare source message IDs.","Verify idempotency handling.","Confirm downstream side effects before replay."]
        confidence = "high"
    else:
        cause = "The available evidence does not identify a single root cause."
        actions = ["Group exceptions by rule code.","Compare with the latest successful batch.","Inspect upstream transformations."]
        confidence = "medium" if request.exceptions else "low"

    return InvestigationResponse(
        batch_id=request.batch_id,
        likely_cause=cause,
        evidence=[f"{e.record_id}: {e.rule_code}" for e in request.exceptions[:5]],
        recommended_actions=actions,
        confidence=confidence,
        retrieved_context=context,
        limitations=["Read-only assistant.","No production systems were queried.","Human review required."]
    )
