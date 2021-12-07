from api.string_interpreter import get_query


def test_default_string_interpretation():
    result = get_query(
        "Finde alle Berge in Berlin die höher als 100m sind"
    )

    assert result.location == "Berlin"
    assert result.query_object == "elevation"
    assert result.route_attributes.height.min == 100


def test_route_keyword():
    result = get_query("Zeige mir einen Weg von Essen nach Köln")
    assert result.query_object == "route"

    result = get_query("Gibt's Routen von der Arktis zur Antarktis")
    assert result.query_object == "route"

    result = get_query("Straße ab Bremershaven")
    assert result.query_object == "route"


def test_place_keyword():
    result = get_query("Ort in NRW")
    assert result.query_object == "place"

    result = get_query("Zeige mir Seen auf der Mecklenburger Seenplatte")
    assert result.query_object == "place"

    result = get_query("Wo liegt der bayrische Wald")
    assert result.query_object == "place"


def test_elevation_keyword():
    result = get_query("Gibt es hohe Hügel in Bayern")
    assert result.query_object == "elevation"

    result = get_query("Erhebungen in Mecklenburg-Vorpommern")
    assert result.query_object == "elevation"

    result = get_query("Zeige mir Berge in Hamburg")
    assert result.query_object == "elevation"


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
    assert result.query_object == "route"  # route is default value
    assert result.route_attributes.height.min == 0
