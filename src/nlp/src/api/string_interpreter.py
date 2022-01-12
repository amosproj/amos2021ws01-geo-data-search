import logging
import os
import pathlib
import sys
from typing import Optional

import spacy
from pydantic.dataclasses import dataclass
from helper_service import convert_number_to_meter

from utils import get_entity_synonyms, get_keyword_from_synonyms, get_alias_synonyms

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
    print(string)
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
            param_1, param_2 = get_query_parameters(index, ner_tokens)
            # check if parameters were found
            if param_2 == "height":
                if index != (len(ner_tokens) - 1):
                    next_token = ner_tokens[index + 1]
                    number = convert_to_meter(token, next_token)
                # last token is an amount but has no unit
                else:
                    number = convert_to_meter(token)
                # select min parameter by default
                if param_1 in ["min", None]:
                    result.route_attributes.height.min = number
                elif param_1 == "max":
                    result.route_attributes.height.max = number
            elif param_2 == "length":
                # select min parameter by default
                if param_1 in ["min", None]:
                    result.route_attributes.length.min = number
                elif param_1 == "max":
                    result.route_attributes.length.max = number
            elif param_2 == "gradiant":
                # select min parameter by default
                if param_1 in ["min", None]:
                    result.route_attributes.gradiant.min = number
                elif param_1 == "max":
                    result.route_attributes.gradiant.max = number
            elif param_2 == "curves_left":
                if param_1 in ["min", None]:
                    result.route_attributes.curves.left.min = number
                elif param_1 == "max":
                    result.route_attributes.curves.left.max = number
            elif param_2 == "curves_right":
                if param_1 in ["min", None]:
                    result.route_attributes.curves.right.min = number
                elif param_1 == "max":
                    result.route_attributes.curves.right.max = number

    # set default value
    if result.query_object == "":
        result.query_object = "route"

    return result


def get_query_parameters(origin: int, tokens: [spacy.tokens.token.Token]) -> (str, str):
    """
    @param origin token which requires the attribute it is referring to
    @param tokens all tokens of the sentence
    @return query attributes found in sting. Example: min height
    """

    dependencies, next_amount, previous_amount = get_dependencies(origin, tokens)
    param_1, param_2 = None, None

    for dep in dependencies:
        lemma = str(dep.lemma_).lower()

        # extract parameter 1
        if param_1 is None:
            if lemma in ["mindestens", "min", "über"]:
                param_1 = "min"
            elif lemma in ["maximal", "max", "höchstens"]:
                param_1 = "max"

        # extract parameter 2
        if param_2 is None:
            if lemma in ["hoch", "höhe", "lage"]:
                param_2 = "height"
            elif lemma in ["lang", "länge"]:
                param_2 = "length"
            elif lemma == "steigung":
                param_2 = "gradiant"
            elif lemma == "linkskurve":
                param_2 = "curves_left"
            elif lemma == "rechtskurve":
                param_2 = "curves_right"

    print("Dependencies for", tokens[origin], ":", dependencies, "->", param_1, param_2)

    return param_1, param_2


def get_dependencies(origin: int, tokens: [spacy.tokens.token.Token], discovery_right: int = 3, discovery_left: int = 3,
                     stop_on_amount: bool = True) -> ([spacy.tokens.token.Token], spacy.tokens.token.Token, spacy.tokens.token.Token):
    """
    Extracts dependencies of a token, starting its search to the left, then to the right
    @param origin: token from which the search is started
    @param tokens: list of tokens containing origin
    @param discovery_right: max search radius to the right
    @param discovery_left: max search radius to the left
    @param stop_on_amount: stop searching in that direction if another token labeled with amount was found
    @return: (list of dependencies, next amount token (if any), previous amount token (if any))
    """

    result = []
    next_token = None
    previous_token = None
    count = len(tokens)

    # look for values to the left of amount object
    for i in range(1, discovery_left + 1):
        # make sure the min array size is not exceeded
        index = origin - i
        if index < 0:
            break

        token = tokens[index]

        # if next token is specifying amount
        if token.ent_type_ == "amount":
            # save it
            if previous_token is None:
                previous_token = token
            # stop search in that direction
            if stop_on_amount:
                break

        result.append(token)

    # look for values to the right of amount object
    for i in range(1, discovery_right + 1):
        # make sure the max array size is not exceeded
        index = origin + i
        if index >= count:
            break

        # get token
        token = tokens[index]

        # if next token is specifying amount
        if token.ent_type_ == "amount":
            # save it
            if next_token is None:
                next_token = token
            # stop search in that direction
            if stop_on_amount:
                break

        result.append(token)

    return result, next_token, previous_token


def check_unit(token: spacy.tokens.token.Token) -> str:
    """
    :param token the token which is checked for a unit
    :return unit, if token has a unit, otherwise an empty string
    """
    synonym = get_keyword_from_synonyms(token.lemma_, "km", unit_synonyms)
    return synonym


def convert_to_meter(
    amount_token: spacy.tokens.token.Token, next_token: spacy.tokens.token.Token = None) -> int:
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


print(get_query("Finde eine Strecke in Italien mit mindestens 10 meilen länge in einer lage über 1000 mit einem Anteil von 500 kilometer Linkskurven mit einem Anteil von 600m Steigung über 7% auf einer Höhe von maximal 10 km"))
# print(get_query("Plane mir eine Route nach Paris mit einem Anteil von 500 meter Steigung von maximal 7%"))
