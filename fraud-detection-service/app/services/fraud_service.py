def calculate_fraud_score(transaction):
    score = 0
    reasons = []

    # High transaction amount
    if transaction.amount > 100000:
        score += 40
        reasons.append("High transaction amount")

    # Suspicious payment method
    if transaction.paymentMethod == "CRYPTO":
        score += 30
        reasons.append("Crypto payment method")

    # Suspicious IP
    if transaction.ipAddress.startswith("192.168"):
        score += 20
        reasons.append("Suspicious IP address")

    # Decision logic
    if score >= 80:
        decision = "BLOCKED"
    elif score >= 50:
        decision = "REVIEW"
    else:
        decision = "APPROVED"

    return {
        "fraudScore": score,
        "decision": decision,
        "reason": reasons
    }