import json

def process_string(string: str) -> str:
    result = Query()

    # hardcoded comparison for testing purposes
    if string == "Finde alle Berge in Berlin die höher als 100m sind":
        result.location = "Berlin"
        result.query_object = "Mountain"
        result.min_height = "100 m"
    
    return result.getjson()

class Query:
    def __init__(self) -> None:
        self.location = ""
        self.max_distance = ""
        self.query_object = ""

        # save route height
        self.max_height = ""
        self.min_height = ""

        # save route length
        self.max_route_length = ""
        self.min_route_length = ""

        # save route gradiant
        self.max_gradiant = ""
        self.min_gradiant = ""
        self.gradiant_length = ""

        # save curve attributes
        # general
        self.min_curves = ""
        self.max_curves = ""
        self.curve_count = ""

        # right curves
        self.min_curves_right = ""
        self.max_curves_right = ""
        self.curve_count_right = ""
        
        # left curves
        self.min_curves_left = ""
        self.max_curves_left = ""
        self.curve_count_left = ""

    
    def getjson(self) -> str:
        dict = {
            "location" : self.location,
            "max distance" : self.max_distance,
            "query object" : self.query_object,
            "route attributes" : {
                "height" : {
                    "min" : self.min_height,
                    "max" : self.max_height
                },
                "length" : {
                    "min" : self.min_route_length,
                    "max" : self.max_route_length
                },
                "gradiant" : {
                    "min" : self.min_gradiant,
                    "max" : self.max_gradiant,
                    "length" : self.gradiant_length
                },
                "curves" : {
                    "min" : self.min_curves,
                    "max" : self.max_curves,
                    "count" : self.curve_count,
                    "right" : {
                        "min" : self.min_curves_right,
                        "max" : self.max_curves_right,
                        "count" : self.curve_count_right,
                    },
                    "left" : {
                        "min" : self.min_curves_left,
                        "max" : self.max_curves_left,
                        "count" : self.curve_count_left,
                    }
                }
            },
        }
        return json.dumps(dict)
