from fastapi import APIRouter

from app.schemas.payment_event import PaymentEvent
from app.services.fraud_service import calculate_fraud_score

router = APIRouter()

@router.post("/fraud/check")
def check_fraud(payment: PaymentEvent):

    result = calculate_fraud_score(payment)

    return {
        "paymentIntentId": payment.paymentIntentId,
        **result
    }