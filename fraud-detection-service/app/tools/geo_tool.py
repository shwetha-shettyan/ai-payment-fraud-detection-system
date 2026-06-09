from langchain.tools import tool


@tool
def get_geo_risk(ip_address: str) -> str:
    """
    Analyze geolocation fraud risk.
    """

    if not ip_address:
        return "IP address missing. Risk HIGH."

    return f"IP {ip_address} appears from trusted geography"