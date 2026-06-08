import os
import joblib
import pandas as pd
import random

from sklearn.ensemble import RandomForestClassifier

MODEL_PATH = "fraud_model.pkl"


def train_model():

    print("Training new fraud detection model...")

    df = pd.read_csv("training-data/fraud_data.csv")

    X = df[
        [
            "amount",
            "velocity_count",
            "device_risk",
            "geo_risk"
        ]
    ]

    y = df["fraud"]

    model = RandomForestClassifier()

    model.fit(X, y)

    joblib.dump(model, MODEL_PATH)

    print("Fraud model trained successfully!")

    return model


# Load existing model OR train new one
if os.path.exists(MODEL_PATH):
    print("Loading existing fraud model...")
    model = joblib.load(MODEL_PATH)
else:
    model = train_model()


def predict_fraud(payment):

    amount = payment.amount
    velocity_count = random.randint(1, 15) if amount > 50000 else 1
    device_risk = round(random.uniform(0, 1), 1) if payment.deviceId else 0.2
    geo_risk = round(random.uniform(0, 1), 1) if payment.ipAddress else 0.2

    features = [[
        amount,
        velocity_count,
        device_risk,
        geo_risk
    ]]

    prediction = model.predict(features)[0]
    probability = model.predict_proba(features)[0][1]

    decision = "APPROVED"

    if probability > 0.8:
        decision = "BLOCKED"
    elif probability > 0.5:
        decision = "REVIEW"

    reasons = []

    if amount > 100000:
        reasons.append("High amount transaction")

    if not payment.deviceId:
        reasons.append("Missing device ID")

    if not payment.ipAddress:
        reasons.append("Missing IP address")

    return {
        "fraudScore": round(probability * 100, 2),
        "decision": decision,
        "reason": reasons
    }