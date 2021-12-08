def convert_number_to_meter(unit: str, number: int) -> str:
    if unit == "m":
        return number
    elif unit == "miles":
        return number * 1609.34
    # assumed default unit is km
    return number * 1000
