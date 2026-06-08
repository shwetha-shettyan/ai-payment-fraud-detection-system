from kafka import KafkaProducer
from kafka.errors import NoBrokersAvailable
import json
import time

producer = None

while producer is None:
    try:
        producer = KafkaProducer(
            bootstrap_servers='kafka:9092',
            value_serializer=lambda v: json.dumps(v).encode('utf-8')
        )
        print("Kafka Producer Connected!")

    except NoBrokersAvailable:
        print("Kafka not ready for producer. Retrying...")
        time.sleep(5)


def publish_fraud_result(result):
    producer.send(
        'fraud-result-topic',
        result
    )

    producer.flush()
    print("Fraud result published:", result)