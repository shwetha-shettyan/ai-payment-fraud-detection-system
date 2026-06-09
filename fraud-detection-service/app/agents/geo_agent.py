def analyze_geo(payment):

    suspicious_ips = [
        "10.0.0.1",
        "172.16.0.1"
    ]

    risk = "LOW"
    score = 5

    if payment.ipAddress in suspicious_ips:
        risk = "HIGH"
        score = 75

    return {
        "agent": "geo-agent",
        "score": score,
        "risk": risk,
        "reason": f"IP analyzed: {payment.ipAddress}"
    }
