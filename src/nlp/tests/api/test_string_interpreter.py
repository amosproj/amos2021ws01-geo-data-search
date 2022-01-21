from api.string_interpreter import get_query, Query
import pytest


def get_query_objects_test_data():
    queries = [
        ["Finde alle Berge in Berlin die höher als 100m sind", "elevation"],
        ["Zeige mir einen Weg von Essen nach Köln", "route"],
        ["Gibt's Routen von der Arktis zur Antarktis", "route"],
        ["Ort in NRW", "place"],
        ["Zeige mir Seen auf der Mecklenburger Seenplatte", "place"],
        ["Wo liegt der bayrische Wald", "place"],
    ]
    return queries


def get_gradient_test_data():
    queries = [
        ["Zeige mir einen Weg von Essen nach Köln mit einer Steigung von maximal 7", "max", 7],
        ["Plane mir eine Route nach Paris mit einer Steigung von über 30%", "min", 30],
        ["Plane mir eine Route nach Paris mit einem Anteil von 500 meter Steigung von maximal 9%", "max", 9, ],
        ["Plane mir eine Route nach Paris mit einer Steigung von mindestens 22%", "min", 22],
        ["Plane mir eine Route nach Paris mit einer Steigung von 8%", "min", 8],
    ]
    return queries


def get_keyword_test_data():
    queries = [
        ["Plane mir eine Route nach Paris mit einer Steigung von maximal 7%", "route"],
        ["Gibt's Routen von der Arktis zur Antarktis", "route"],
        ["Straße ab Bremershaven", "route"],
        ["Ort in NRW", "place"],
        ["Zeige mir Seen auf der Mecklenburger Seenplatte", "place"],
        ["Wo liegt der bayrische Wald", "place"],
        ["Gibt es hohe Hügel in Bayern", "elevation"],
        ["Erhebungen in Mecklenburg-Vorpommern", "elevation"],
        ["Zeige mir Berge in Hamburg", "elevation"],
        ["Wo sind Almen in Brandenburg", "route"],
        ["Restaurants in Spanien", "route"],
        ["Lage des Kieler Hafens", "route"]

    ]
    return queries


def get_charging_station_test_data():
    queries = [
        ["Plane mir eine Route nach Paris mit einer Steigung von maximal 7% mit ladestationen", True],
        ["Gibt's Routen von der Arktis zur Antarktis", False],
        ["Plane mir eine Route nach Paris ohne ladesäulen", False],
        ["Plane mir eine Route nach Paris mit einer Steigung von mindestens 22% mit etankstellen", True],
        ["Plane mir eine Route nach Paris mit Stromtankstellen", True],
        ["Plane mir eine Route nach Paris mit keinen charging stations", False],
    ]
    return queries


def get_toll_roads_test_data():
    queries = [
        ["Plane mir eine Route nach Paris mit einer Steigung von maximal 7% ohne mautstellen", True],
        ["Gibt es kostenfreie Routen von der Arktis zur Antarktis", True],
        ["Plane mir eine Route nach Paris mit maustellen", False],
        ["Plane mir eine Route nach Paris mit einer Steigung von mindestens 22% mit etankstellen und maut", False],
        ["Plane mir eine Route nach Paris mit gebühren", False],
        ["Plane mir eine Route nach Paris mit gebühr", False],
        ["Plane mir eine Route nach Paris ohne Gebühr", True],
        ["Plane mir eine Route nach Paris mit zollstellen", False],
        ["Plane mir eine gebührenfreie Route nach Paris", True],
        ["Plane mir eine Route nach Paris mit Mautstraßen", False],
        ["Plane mir eine Route nach Paris mit keinen mautstellen", True],
        ["Plane mir eine Route nach Paris ohne gebühren zu zahlen", True],
        ["Plane mir eine Route nach Paris mit Autobahnen, kann auch gebühren enthalten", False],
        ["Plane mir eine Route nach Paris ohne Mautstraßen", True],
        ["Plane mir eine Route nach Paris", False],
        ["Plane mir eine kostenlose Route nach Paris", True]
    ]
    return queries


def get_query_test_data():
    queries = []
    query = Query()
    query.location = "Paris"
    query.query_object = "route"
    query.route_attributes.gradiant.max = 7
    queries.append(["Plane mir eine Route nach Paris mit einer Steigung von maximal 7%", query])

    query = Query()
    query.location = "Essen, Köln"
    query.query_object = "route"
    query.route_attributes.gradiant.max = 7
    queries.append(["Zeige mir einen Weg von Essen nach Köln mit einer Steigung von maximal 7", query])

    query = Query()
    query.location = "Berlin"
    query.query_object = "elevation"
    query.route_attributes.height.min = 1000
    queries.append(["Gibt es Hügel in Berlin mit einer Höhe von mindestens 1000 metern", query])

    query = Query()
    query.location = "Spanien"
    query.query_object = "route"
    query.route_attributes.length.min = 1000
    queries.append(["Finde eine Strecke in Spanien mit einer Länge von 10 kilometern in einer lage über 1000m", query])

    query = Query()
    query.location = "Hamburg"
    query.query_object = "elevation"
    query.route_attributes.height.min = 1609
    queries.append([" Zeige mir Berge mit einer Höhe von 1 meile in Hamburg", query])
    return queries


@pytest.mark.parametrize("query", get_query_objects_test_data())
def test_query_objects(query):
    result = get_query(query[0])
    assert result.query_object == query[1]


@pytest.mark.parametrize("query", get_gradient_test_data())
def test_route_gradient(query):
    result = get_query(query[0])
    assert getattr(result.route_attributes.gradiant, query[1]) == query[2]


@pytest.mark.parametrize("query", get_keyword_test_data())
def test_query_objects_keyword(query):
    result = get_query(query[0])
    assert result.query_object == query[1]


@pytest.mark.parametrize("query", get_query_test_data())
def test_query(query):
    result = get_query(query[0])
    assert result == query[1]


@pytest.mark.parametrize("query", get_charging_station_test_data())
def test_charging_station(query):
    result = get_query(query[0])
    assert result.route_attributes.charging_stations == query[1]


@pytest.mark.parametrize("query", get_toll_roads_test_data())
def test_toll_roads(query):
    result = get_query(query[0])
    assert result.route_attributes.toll_road_avoidance == query[1]


def test_default_keyword():
    result = get_query("Wo sind Almen in Brandenburg")
    assert result.query_object == "route"

    result = get_query("Restaurants in Spanien")
    assert result.query_object == "route"

    result = get_query("Lage des Kieler Hafens")
    assert result.query_object == "route"


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


def test_convert_units():
    result = get_query("Zeige mir Berge in Hamburg mit einer Höhe von 1 kilometern")
    assert result.route_attributes.height.min == 1000

    result = get_query("Zeige mir Berge mit einer Höhe von 1 meile in Hamburg")
    assert result.route_attributes.height.min == int(1609.34)

    # without a unit, it should km should be assumed as unit
    result = get_query("Berge in Berlin Höhe von maximal 1")
    assert result.route_attributes.height.max == 1000

    # without a unit, it should km should be assumed as unit
    result = get_query("Route von Berlin nach Bremen mit Länge maximal 500")
    assert result.route_attributes.length.max == 500000

    result = get_query("Route von Berlin nach Bremen mit Länge maximal 500 km")
    assert result.route_attributes.length.max == 500000

    result = get_query("Route von Berlin nach Bremen mit Länge mindestens 50 km")
    assert result.route_attributes.length.min == 50000


def test_no_input():
    result = get_query("")

    assert result.location == ""
    assert result.query_object == "route"  # route is default value
    assert result.route_attributes.height.min == 0


def test_route_length():
    result = get_query("Plane mir eine Route nach Paris mit einer länge von mindestens 1 und maximal 1100 metern")

    assert result.route_attributes.length.min == 1
    assert result.route_attributes.length.max == 1100


def test_route_length_split_parameter():
    result = get_query("Plane mir eine Route nach Paris mit einer länge von mindestens 2 und maximal 20 km")

    assert result.route_attributes.length.min == 2000
    assert result.route_attributes.length.max == 20000
