def convert_number_to_meter(unit: str, number: int) -> str:
    if unit == "m":
        return number
    elif unit == "km":
        return number * 1000
    elif unit == "miles":
        return number * 1609.34
    return ""
