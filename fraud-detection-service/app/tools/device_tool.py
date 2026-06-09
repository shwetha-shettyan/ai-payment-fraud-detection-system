from langchain.tools import tool


@tool
def get_device_risk(device_id: str) -> str:
    """
    Analyze device fraud risk.
    """

    if not device_id:
        return "Device ID missing. Risk HIGH."

    return f"Device {device_id} associated with low fraud activity"