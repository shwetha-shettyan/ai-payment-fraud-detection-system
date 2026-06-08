from fastapi import FastAPI
import threading

from app.kafka.consumer import start_consumer

app = FastAPI()

@app.get("/")
def health():
    return {"status": "running"}

threading.Thread(
    target=start_consumer,
    daemon=True
).start()
