def convert_number_to_meter(unit: str, number: int) -> int:
    """
    Converts input number into meter
    :param unit Unit of number
    :param number Integer to be converted
    """

    # meters is default unit
    if unit == "m":
        return number

    # convert miles to meters
    if unit == "miles":
        return int(number * 1609.34)

    # convert km to meters
    if unit == "km":
        return number * 1000

    return number
