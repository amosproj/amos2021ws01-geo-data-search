import logging
import os
import pathlib
import sys
from typing import Optional

import spacy
from pydantic.dataclasses import dataclass
from .helper_service import convert_number_to_meter

from .utils import get_entity_synonyms, get_keyword_from_synonyms, get_alias_synonyms

# get os specific file separator
SEP = os.path.sep

current_dir = pathlib.Path(__file__).parent.resolve()

# load spacy ml nlp model
nlp_default = spacy.load("de_core_news_sm")


def load_custom_ner_model():
    try:
        # this path is valid when this class is run locally
        model = spacy.load(f"{current_dir}{SEP}..{SEP}models{SEP}training{SEP}")
        return model
    except IOError as error:
        sys.exit(str(error) + "\nML model was not trained locally")


# load custom ml ner model
ner_model = load_custom_ner_model()

# get synonyms for keywords from chatette file
query_object_synonyms = get_entity_synonyms(entity="queryObject")["queryObject"]
unit_synonyms = get_alias_synonyms(title="unit")["unit"]


def process_string(string: str) -> object:
    # analyse preprocessed string
    return get_query(string)


def get_query(string: str) -> object:
    default_tokens = nlp_default(string)

    result = Query()

    for token in default_tokens:
        # save found location
        if token.ent_type_ == "LOC":
            if result.location != "":
                result.location += ", "
            result.location += token.lemma_

    ner_tokens = ner_model(string)
    for index in range(len(ner_tokens)):
        token = ner_tokens[index]

        # save query object
        if token.ent_type_ == "queryObject":
            result.query_object = get_keyword_from_synonyms(token.lemma_, "route", query_object_synonyms)

        # extract information about amount parameter
        if token.ent_type_ == "amount":
            # save number as string
            number = str(token.lemma_)

            # make sure an int was found
            if not number.isnumeric():
                continue

            # transform number string to int
            number = int(number)

            # extract query parameters
            param_1, param_2 = get_query_parameters(token)
            # check if parameters were found
            if param_2 == "height":
                if index != (len(ner_tokens) - 1):
                    next_token = ner_tokens[index + 1]
                    number = convert_to_meter(token, next_token)
                # last token is an amount but has no unit
                else:
                    number = convert_to_meter(token)
                # select min parameter by default
                if param_1 == "min" or param_1 == "":
                    result.route_attributes.height.min = number
                elif param_1 == "max":
                    result.route_attributes.height.max = number
            elif param_2 == "length":
                # select min parameter by default
                if param_1 == "min" or param_1 == "":
                    result.route_attributes.length.min = number
                elif param_1 == "max":
                    result.route_attributes.length.max = number
            elif param_2 == "gradiant":
                # select min parameter by default
                if param_1 == "min" or param_1 == "":
                    result.route_attributes.gradiant.min = number
                elif param_1 == "max":
                    result.route_attributes.gradiant.max = number

    # set default value
    if result.query_object == "":
        result.query_object = "route"

    return result


def get_query_parameters(origin: spacy.tokens.token.Token) -> (str, str):
    """
    :param origin token which requires the attribute it is referring to
    :return query attributes found in sting. Example: min height
    """

    dependencies = get_dependencies(origin)
    param_1, param_2 = "", ""

    for dep in dependencies:
        lemma = str(dep.lemma_).lower()

        # extract parameter 1
        if param_1 == "":
            if lemma in ["mindestens", "min"]:
                param_1 = "min"
            elif lemma in ["maximal", "max", "höchstens"]:
                param_1 = "max"
            elif lemma == "über":
                param_1 = "min"

        # extract parameter 2
        if param_2 == "":
            if lemma in ["hoch", "höhe"]:
                param_2 = "height"
            elif lemma in ["lang", "länge"]:
                param_2 = "length"
            elif lemma == "steigung":
                param_2 = "gradiant"
    return param_1, param_2


def get_dependencies(origin: spacy.tokens.token.Token) -> [spacy.tokens.token.Token]:
    """
    :param origin token which requires its closest dependencies
    :return tokens ordered by proximity to origin
    """

    # initialise list of results
    results = []

    # initialise list of tokens to explore
    discovery_queue = [origin]

    # search through entire dependency tree, ordering elements by vicinity to origin
    while len(discovery_queue) > 0:
        # get next token to process
        current_token = discovery_queue.pop(0)

        # add current token to results
        results.append(current_token)

        # add children to discovery queue, if it wasnt discovered already
        for child in current_token.children:
            if child not in discovery_queue and child not in results:
                discovery_queue.append(child)

        # add head to discovery queue, if it wasn't discovered already
        current_head = current_token.head
        if current_head not in discovery_queue and current_head not in results:
            discovery_queue.append(current_head)

    return results


def check_unit(token: spacy.tokens.token.Token) -> str:
    """
    :param token the token which is checked for a unit
    :return unit, if token has a unit, otherwise an empty string
    """
    synonym = get_keyword_from_synonyms(token.lemma_, "km", unit_synonyms)
    return synonym


def convert_to_meter(
    amount_token: spacy.tokens.token.Token, next_token: spacy.tokens.token.Token = None
) -> int:
    if next_token is None:
        amount_unit = "km"
    else:
        amount_unit = check_unit(next_token)
    if amount_unit != "":
        number = int(amount_token.text)
        converted_number = convert_number_to_meter(amount_unit, number)
        return converted_number
    return 0


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


# print(get_query("Finde eine Strecke in Italien mit mindestens 10 meilen länge in einer lage über 1000  mit einem Anteil von 500 kilometer Linkskurven mit einem Anteil von 600m Steigung über 7% auf einer Höhe von maximal 10"))
print(
    get_query(
        "Plane mir eine Route nach Paris mit einem Anteil von 500 meter Steigung von maximal 7%"
    )
)
