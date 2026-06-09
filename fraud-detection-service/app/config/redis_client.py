import redis
import time

redis_client = None

while redis_client is None:
    try:
        redis_client = redis.Redis(
            host="redis",
            port=6379,
            decode_responses=True
        )

        redis_client.ping()

        print("Connected to Redis!")

    except Exception as ex:
        print("Redis not ready. Retrying...", ex)
        time.sleep(5)
