def convert_number_to_meter(unit: str, number: int) -> int:
    """
    Converts input number into meter
    :param unit Unit of number
    :param number Integer to be converted
    """
    if unit == "m":
        return number
    if unit == "miles":
        return int(number * 1609.34)
    # assumed default unit is km
    return number * 1000
