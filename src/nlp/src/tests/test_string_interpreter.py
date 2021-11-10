from packages import api
from api import string_interpreter


def test_default_string_interpretation():
    result = string_interpreter.get_query(
        "Finde alle Berge in Berlin die höher als 100m sind"
    )

    assert result.location == "Berlin"
    assert result.query_object == "Mountain"
    assert result.min_height == "100 m"


def test_no_input():
    result = string_interpreter.get_query("")

    assert result.location == ""
    assert result.query_object == ""
    assert result.min_height == ""
