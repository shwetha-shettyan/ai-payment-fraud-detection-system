from app.services.ml_fraud_service import predict_fraud


def analyze_ml(payment):

    result = predict_fraud(payment)

    score = float(result["fraudScore"])

    risk = "LOW"

    if score >= 70:
        risk = "HIGH"
    elif score >= 40:
        risk = "MEDIUM"

    return {
        "agent": "ml-agent",
        "score": score,
        "risk": risk,
        "reason": f"ML fraud score = {score}"
    }
