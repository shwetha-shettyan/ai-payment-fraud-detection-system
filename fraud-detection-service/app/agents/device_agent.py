def analyze_device(payment):

    risky_devices = [
        "emulator",
        "root-device",
        "unknown-device"
    ]

    risk = "LOW"
    score = 10

    if payment.deviceId in risky_devices:
        risk = "HIGH"
        score = 80

    return {
        "agent": "device-agent",
        "score": score,
        "risk": risk,
        "reason": f"Device checked: {payment.deviceId}"
    }
