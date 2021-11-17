from typing import Optional

import spacy
from pydantic.dataclasses import dataclass

# load spacy ml nlp model
nlp = spacy.load("de_core_news_sm")


def process_string(string: str) -> object:
    # analyse preprocessed string
    return get_query(string)


def preprocess_string(string: str) -> str:
    # analyse string
    tokens = nlp(string)

    # combine lemmata into string
    preprocessed_string = ""

    for token in tokens:
        # separate tokens with space
        if preprocessed_string != "":
            preprocessed_string += " "
        preprocessed_string += token.lemma_
    return preprocessed_string


def get_query(string: str) -> object:
    string = preprocess_string(string)

    result = Query()

    # hardcoded comparison for testing purposes
    if string == "Finde all Berg in Berlin der hoch als 100 m sein":
        result.location = "Berlin"
        result.query_object = "Mountain"
        result.route_attributes.height.min = 100
        result.route_attributes.curves.left.count = 5

    return result


@dataclass
class BaseAttributes:
    min: Optional[int]
    max: Optional[int]

    def __init__(self):
        self.min = 0
        self.max = 0


@dataclass
class GradiantAttributes(BaseAttributes):
    length: Optional[int]

    def __init__(self):
        super().__init__()
        self.length = 0


@dataclass
class CurveAttributes(BaseAttributes):
    count: Optional[int]

    def __init__(self):
        super().__init__()
        self.count = 0


@dataclass
class Curves(CurveAttributes):
    left: Optional[CurveAttributes]
    right: Optional[CurveAttributes]

    def __init__(self):
        super().__init__()
        self.left = CurveAttributes()
        self.right = CurveAttributes()


@dataclass
class RouteAttributes:
    height: Optional[BaseAttributes]
    length: Optional[BaseAttributes]
    gradiant: Optional[GradiantAttributes]
    curves: Optional[Curves]

    def __init__(self):
        self.height = BaseAttributes()
        self.length = BaseAttributes()
        self.gradiant = GradiantAttributes()
        self.curves = Curves()


@dataclass
class Query:
    location: Optional[str]
    max_distance: Optional[int]
    query_object: str
    route_attributes: Optional[RouteAttributes]

    def __init__(self) -> None:
        self.location = ""
        self.max_distance = 0
        self.query_object = ""

        self.route_attributes = RouteAttributes()
