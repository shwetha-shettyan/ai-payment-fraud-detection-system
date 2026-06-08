from pydantic import BaseModel

class FraudResponse(BaseModel):

    paymentIntentId: str

    fraudScore: float

    decision: str

    reason: list[str]