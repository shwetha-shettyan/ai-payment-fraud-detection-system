from langchain.tools import tool
from app.config.redis_client import redis_client

WINDOW_SECONDS = 180


@tool
def get_velocity(user_id: str) -> str:
    """
    Get transaction velocity for a user.
    """

    key = f"txn:{user_id}"

    current_count = redis_client.incr(key)

    if current_count == 1:
        redis_client.expire(key, WINDOW_SECONDS)

    return f"User made {current_count} transactions in last 3 minutes"
