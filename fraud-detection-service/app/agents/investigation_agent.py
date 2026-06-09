from dotenv import load_dotenv

from langchain_groq import ChatGroq

load_dotenv()

llm = ChatGroq(
    model="llama-3.3-70b-versatile",
    temperature=0
)

def investigate_transaction(agent_results):
    prompt = f"""
    You are a senior fraud investigation analyst.
    Analyze the following fraud signals:
    {agent_results}

    Rules:
    - High velocity increases fraud risk significantly
    - Trusted device lowers fraud risk
    - Trusted geography lowers fraud risk
    - ML score indicates statistical fraud probability

    Decide one:
    - APPROVED
    - REVIEW
    - BLOCKED

    Return response in this format:
    Decision: <decision>
    Explanation:
    <detailed explanation>
    """

    response = llm.invoke(prompt)

    return response.content
