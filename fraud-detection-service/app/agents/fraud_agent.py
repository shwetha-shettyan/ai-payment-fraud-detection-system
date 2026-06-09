from dotenv import load_dotenv

from langchain_groq import ChatGroq
from langgraph.prebuilt import create_react_agent

from app.tools.velocity_tool import get_velocity
from app.tools.device_tool import get_device_risk
from app.tools.geo_tool import get_geo_risk

load_dotenv()

llm = ChatGroq(
    model="llama-3.3-70b-versatile",
    temperature=0
)

tools = [
    get_velocity,
    get_device_risk,
    get_geo_risk
]

agent = create_react_agent(
    model=llm,
    tools=tools
)


def investigate(payment):

    query = f"""
    You are an AI fraud investigation agent.

    Investigate this payment transaction.

    paymentIntentId: {payment.paymentIntentId}
    userId: {payment.userId}
    amount: {payment.amount}
    deviceId: {payment.deviceId}
    ipAddress: {payment.ipAddress}

    Use the available tools to: 
    - check transaction velocity 
    - evaluate device risk 
    - evaluate geo risk

    Decide:
    APPROVED, REVIEW, or BLOCKED.

    Explain reasons.
    """

    response = agent.invoke({
        "messages": [
            {
                "role": "user",
                "content": query
            }
        ]
    })

    return response["messages"][-1].content
