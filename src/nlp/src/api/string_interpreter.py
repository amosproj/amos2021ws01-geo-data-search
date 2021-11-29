import pathlib
import sys
from typing import Optional

import spacy
from pydantic.dataclasses import dataclass

current_dir = pathlib.Path(__file__).parent.resolve()

# load spacy ml nlp model
nlp_default = spacy.load("de_core_news_sm")

# load custom ml ner model
try:
    # this path is valid when this class is run locally
    ner_model = spacy.load(f"{current_dir}/../models/training/")
except IOError as error:
    sys.exit(str(error) + "\nML model was not trained locally")


def process_string(string: str) -> object:
    # analyse preprocessed string
    return get_query(string)


def get_query(string: str) -> object:
    tokens = nlp_default(string)

    result = Query()

    for token in tokens:
        # save found location
        if token.ent_type_ == "LOC":
            if result.location != "":
                result.location += ", "
            result.location += token.lemma_

    tokens = ner_model(string)
    for token in tokens:
        # save query object
        if token.ent_type_ == "queryObject":
            result.query_object = get_synonym(token.lemma_)
        # save min height
        if token.ent_type_ == "amount":
            # make sure an int was found
            if not str(token.lemma_).isnumeric():
                continue

            # todo: extract which attribute the amount is specifying
            result.route_attributes.height.min = int(token.lemma_)
        print(token.lemma_, ":", token.ent_type_)

    return result


def get_synonym(string : str) -> str:
    if string == "Berg":
        return "Mountain"
    return string

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
