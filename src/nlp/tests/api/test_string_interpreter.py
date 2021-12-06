from api.string_interpreter import get_query


def test_default_string_interpretation():
    result = get_query(
        "Finde alle Berge in Berlin die höher als 100m sind"
    )

    assert result.location == "Berlin"
    assert result.query_object == "elevation"
    assert result.route_attributes.height.min == 100


def test_route_keyword():
    assert "route" == get_query("").query_object
    assert "route" == get_query("").query_object
    assert "route" == get_query("").query_object


def test_place_keyword():
    assert "place" == get_query("").query_object
    assert "place" == get_query("").query_object
    assert "place" == get_query("").query_object


def test_elevation_keyword():
    assert "elevation" == get_query("").query_object
    assert "elevation" == get_query("").query_object
    assert "elevation" == get_query("").query_object


def test_default_keyword():
    result = get_query("Wo sind Almen in Brandenburg")
    assert result.query_object == "route"

    result = get_query("Restaurants in Spanien")
    assert result.query_object == "route"

    result = get_query("Lage des Kieler Hafens")
    assert result.query_object == "route"


def test_no_input():
    result = get_query("")

    assert result.location == ""
    assert result.query_object == ""
    assert result.route_attributes.height.min == 0
