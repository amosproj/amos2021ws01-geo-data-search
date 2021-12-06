import os
import pathlib
import sys
from typing import Optional

import spacy
from pydantic.dataclasses import dataclass

current_dir = pathlib.Path(__file__).parent.resolve()

# get os specific file separator
SEP = os.path.sep

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
    for token in ner_tokens:
        # save query object
        if token.ent_type_ == "queryObject":
            result.query_object = get_synonym(token.lemma_)
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
                # select min parameter by default
                if param_1 == "min" or param_1 == "":
                    result.route_attributes.height.min = number
                elif param_1 == "max":
                    result.route_attributes.height.max = number


    return result


def get_synonym(string: str) -> str:
    if string == "Berg":
        return "Mountain"
    return string

def get_query_parameters(origin : spacy.tokens.token.Token) -> (str, str):
    """
    :param origin token which requires the attribute it is referring to
    :return query attributes found in sting. Example: min height
    """

    dependencies = get_depencies(origin)
    param_1, param_2 = "", ""

    for dep in dependencies:
        lemma = str(dep.lemma_).lower()

        # extract parameter 1
        if param_1 == "":
            if lemma == "mindestens":
                param_1 = "min"
            elif lemma == "maximal":
                param_1 = "max"

        # extract parameter 2
        if param_2 == "":
            if lemma == "hoch" or lemma == "höhe":
                param_2 = "height"

    return param_1, param_2


def get_depencies(origin : spacy.tokens.token.Token) -> [spacy.tokens.token.Token]:
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
