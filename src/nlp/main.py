import json

def process_string(string: str) -> str:
    result = Query()

    # hardcoded comparison for testing purposes
    if string == "Finde alle Berge in Berlin die höher als 100m sind":
        result.location = "Berlin"
        result.query_object = "Mountain"
        result.min_route_length = "100 m"
    
    print(result.getjson())

    return ""

class Query:
    def __init__(self) -> None:
        self.location = ""
        self.max_distance = ""
        self.query_object = ""

        # save attributes
        self.max_height = ""
        self.min_height = ""

        # save route length
        self.max_route_length = ""
        self.min_route_length = ""
    
    def getjson(self) -> str:
        dict = {
            "location" : self.location,
            "max distance" : self.max_distance,
            "query object" : self.query_object,
            "attributes" : {
                "height" : {
                    "min" : self.min_height,
                    "max" : self.max_height
                }
            },
            "route length" : {
                "min" : self.min_route_length,
                "max" : self.max_route_length
            }
        }
        return json.dumps(dict)

process_string("Finde alle Berge in Berlin die höher als 100m sind2")
