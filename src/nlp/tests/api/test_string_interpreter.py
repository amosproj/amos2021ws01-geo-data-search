from api.string_interpreter import get_query


def test_default_string_interpretation():
    result = get_query("Finde alle Berge in Berlin die höher als 100m sind")

    assert result.location == "Berlin"
    assert result.query_object == "elevation"
    assert result.route_attributes.height.min == 100


def test_long_string_interpretation():
    result = get_query(
        "Finde eine Strecke in Italien mit mindestens 10km länge in einer lage über 1000m mit einem Anteil von 500m Linkskurven mit einem Anteil von 600m Steigung über 7% auf einer Höhe von maximal 10000 Metern"
    )

    assert result.location == "Italien"
    assert result.query_object == "route"
    assert result.route_attributes.height.max == 10000


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


def test_convert_units():
    result = get_query("Zeige mir Berge in Hamburg mit einer Höhe von 1 kilometern")
    assert result.route_attributes.height.min == 1000

    result = get_query("Zeige mir Berge mit einer Höhe von 1 meile in Hamburg")
    assert result.route_attributes.height.min == int(1609.34)

    result = get_query("Berge in Berlin Höhe von maximal 1")
    assert result.route_attributes.height.max == 1000


def test_no_input():
    result = get_query("")

    assert result.location == ""
    assert result.query_object == "route"  # route is default value
    assert result.route_attributes.height.min == 0


def test_route_length():
    result = get_query(
        "Plane mir eine Route nach Paris mit einer länge von mindestens 100 und maximal 1000 metern"
    )

    assert result.route_attributes.length.min == 100
    assert result.route_attributes.length.max == 1000


def test_route_gradient():
    result = get_query(
        "Plane mir eine Route nach Paris mit einer Steigung von maximal 7%"
    )

    assert result.route_attributes.gradiant.max == 7

    result = get_query(
        "Plane mir eine Route nach Paris mit einer Steigung von über 7% "
    )

    assert result.route_attributes.gradiant.min == 7

    result = get_query(
        "Plane mir eine Route nach Paris mit einem Anteil von 500 meter Steigung von maximal 7%"
    )

    assert result.route_attributes.gradiant.max == 7
