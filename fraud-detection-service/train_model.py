import pandas as pd
from sklearn.ensemble import RandomForestClassifier
import joblib

# Load dataset
df = pd.read_csv("training-data/fraud_data.csv")

# Features
X = df[
    [
        "amount",
        "velocity_count",
        "device_risk",
        "geo_risk"
    ]
]

# Labels
y = df["fraud"]

# Train model
model = RandomForestClassifier()

model.fit(X, y)

# Save model
joblib.dump(model, "fraud_model.pkl")

print("Fraud model trained successfully!")