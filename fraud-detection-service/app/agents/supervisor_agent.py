from app.agents.velocity_agent import analyze_velocity
from app.agents.device_agent import analyze_device
from app.agents.geo_agent import analyze_geo
from app.agents.ml_agent import analyze_ml
from app.agents.investigation_agent import investigate_transaction


def run_fraud_investigation(payment):

    results = [
        analyze_velocity(payment),
        analyze_device(payment),
        analyze_geo(payment),
        analyze_ml(payment)
    ]

    total_score = sum(r["score"] for r in results) / len(results)

    decision = "APPROVED"

    if total_score >= 70:
        decision = "BLOCKED"
    elif total_score >= 40:
        decision = "REVIEW"

    # LLM Investigation 
    investigation_report = investigate_transaction(results)

    return {
        "decision": decision,
        "fraudScore": round(total_score, 2),
        "agentResults": results, 
        "investigationReport": investigation_report
    }
