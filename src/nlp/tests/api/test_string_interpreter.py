import sys

from api.string_interpreter import get_query

def test_default_string_interpretation():
    result = get_query(
        "Finde alle Berge in Berlin die höher als 100m sind"
    )

    assert result.location == "Berlin"
    assert result.query_object == "Mountain"
    assert result.route_attributes.height.min == 100


def test_no_input():
    result = get_query("")

    assert result.location == ""
    assert result.query_object == ""
    assert result.route_attributes.height.min == 0
