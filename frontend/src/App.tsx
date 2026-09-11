import { useState } from "react";

export default function App() {
  const [result,setResult] = useState<any>(null);
  const [analysis,setAnalysis] = useState<any>(null);
  const [busy,setBusy] = useState(false);

  async function runDemo() {
    setBusy(true);
    const r = await fetch("http://localhost:8081/api/v1/validation/batches",{
      method:"POST",headers:{"Content-Type":"application/json"},
      body:JSON.stringify({
        source:"portfolio-demo",
        records:[
          {recordId:"ITEM-1001",sku:"SKU-1001",market:"CA",effectiveDate:"2026-09-11",price:14.99,status:"ACTIVE",sourceSystem:"planner"},
          {recordId:"ITEM-1002",sku:"SKU-1002",market:"CA",effectiveDate:"2026-09-11",price:-1,status:"ACTIVE",sourceSystem:"planner"}
        ]
      })
    });
    setResult(await r.json()); setBusy(false);
  }

  async function investigate() {
    setBusy(true);
    const r = await fetch("http://localhost:8000/api/v1/investigate",{
      method:"POST",headers:{"Content-Type":"application/json"},
      body:JSON.stringify({
        batch_id:result.batchId,source:result.source,
        exceptions:result.issues.map((i:any)=>({record_id:i.recordId,rule_code:i.ruleCode,field:i.field,message:i.message}))
      })
    });
    setAnalysis(await r.json()); setBusy(false);
  }

  return <main>
    <p className="eyebrow">JAVA + SPRING BOOT + PYTHON + REACT + AI</p>
    <h1>Enterprise AI Operations Platform</h1>
    <p className="subtitle">Spring Boot microservices for operational validation and reconciliation, with a Python AI investigation service and React UI.</p>
    <button onClick={runDemo}>{busy?"Working...":"Run demo validation"}</button>

    <section className="cards">
      <article><span>Backend</span><strong>Java 21 / Spring Boot</strong></article>
      <article><span>AI</span><strong>Python / FastAPI</strong></article>
      <article><span>Events</span><strong>Kafka</strong></article>
      <article><span>UI</span><strong>React / TypeScript</strong></article>
    </section>

    {result && <section className="panel">
      <h2>{result.status}</h2>
      <p>{result.totalRecords} records · {result.invalidRecords} invalid</p>
      {result.issues.map((i:any)=><div className="issue" key={i.recordId+i.ruleCode}><strong>{i.ruleCode}</strong><p>{i.message}</p></div>)}
      <button onClick={investigate}>Investigate with AI</button>
    </section>}

    {analysis && <section className="panel">
      <p className="eyebrow">AI INVESTIGATION</p>
      <h2>{analysis.likely_cause}</h2>
      <p>Confidence: <strong>{analysis.confidence}</strong></p>
      <ol>{analysis.recommended_actions.map((a:string)=><li key={a}>{a}</li>)}</ol>
    </section>}
  </main>;
}
