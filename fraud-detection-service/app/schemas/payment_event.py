from pydantic import BaseModel
from typing import Optional

class PaymentEvent(BaseModel):
    paymentIntentId: str
    userId: str
    amount: float
    currency: str
    paymentMethod: str
    merchant: str
    ipAddress: Optional[str] = None 
    deviceId: Optional[str] = None
    timestamp: int
