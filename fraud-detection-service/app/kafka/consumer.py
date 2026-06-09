from kafka import KafkaConsumer
from kafka.errors import NoBrokersAvailable
import json
import time
import traceback

from app.schemas.payment_event import PaymentEvent
#from app.services.fraud_service import calculate_fraud_score
from app.services.ml_fraud_service import predict_fraud
from app.kafka.producer import publish_fraud_result
from app.agents.fraud_agent import investigate
from app.agents.supervisor_agent import run_fraud_investigation

def start_consumer(): 
    consumer = None 
    while consumer is None: 
        try: 
            print("Attempting Kafka connection...", flush=True) 
            consumer = KafkaConsumer( 
                'payment-topic', 
                bootstrap_servers='kafka:9092', 
                group_id='fraud-detection-group', 
                value_deserializer=lambda m: json.loads(m.decode('utf-8')) 
            ) 
            print("Connected to Kafka successfully!", flush=True) 
        except NoBrokersAvailable: 
            print("Kafka not ready. Retrying...", flush=True) 
            time.sleep(5) 
            
        print("Fraud Detection Consumer Started...", flush=True) 
        for message in consumer: 
            try: 
                print("MESSAGE RECEIVED", flush=True) 
                data = message.value 
                print("Received Payment Event:", data, flush=True) 
                payment = PaymentEvent(**data) 
                #result = calculate_fraud_score(payment) 
                
                # ML Investigation
                #result = predict_fraud(payment)

                # Agent Code
                #agent_result = investigate(payment)
                #print("AGENT INVESTIGATION:")
                #print(agent_result)
                
                # Multi-Agent INVESTIGATION
                agent_result = run_fraud_investigation(payment)
                print("\n MULTI AGENT RESULT:") 
                print(agent_result)
                
                print("\nINVESTIGATION REPORT:") 
                print(agent_result["investigationReport"])

                result_payload = { 
                    "paymentIntentId": payment.paymentIntentId, 
                    #**result 
                    "fraudScore": agent_result["fraudScore"], 
                    "decision": agent_result["decision"], 
                    "reason": [ 
                        r["reason"] for r in agent_result["agentResults"] 
                    ]
                } 
                print("Fraud Result:", result_payload, flush=True) 
                publish_fraud_result(result_payload) 
            except Exception: 
                traceback.print_exc()
