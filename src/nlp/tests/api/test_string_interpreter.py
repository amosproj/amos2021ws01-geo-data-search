from api.string_interpreter import get_query


def test_default_string_interpretation():
    result = get_query(
        "Finde alle Berge in Berlin die höher als 100m sind"
    )

    assert result.location == "Berlin"
    assert result.query_object == "Mountain"
    assert result.route_attributes.height.min == 100


def test_long_string_interpretation():
    result = get_query(
        "Finde eine Strecke in Italien mit mindestens 10km länge in einer lage über 1000m mit einem Anteil von 500m Linkskurven mit einem Anteil von 600m Steigung über 7% auf einer Höhe von maximal 10000 Metern"
    )

    assert result.location == "Italien"
    assert result.query_object == "Strecke"
    assert result.route_attributes.height.max == 10000


def test_no_input():
    result = get_query("")

    assert result.location == ""
    assert result.query_object == ""
    assert result.route_attributes.height.min == 0
