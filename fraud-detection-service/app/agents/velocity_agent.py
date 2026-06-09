from app.config.redis_client import redis_client


def analyze_velocity(payment):

    key = f"txn:{payment.userId}"

    txn_count = redis_client.incr(key)

    redis_client.expire(key, 180)

    score = min(txn_count * 20, 100)

    risk = "LOW"

    if score >= 70:
        risk = "HIGH"
    elif score >= 40:
        risk = "MEDIUM"

    return {
        "agent": "velocity-agent",
        "score": score,
        "risk": risk,
        "reason": f"{txn_count} transactions in last 3 minutes"
    }