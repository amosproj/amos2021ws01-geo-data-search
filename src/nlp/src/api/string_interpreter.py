import logging
LOGGER = logging.getLogger("[STRING INTERPRETER]")

import os
import pathlib
import sys
from typing import Optional

import spacy
from pydantic.dataclasses import dataclass

from .helper_service import convert_number_to_meter, check_similarity, check_similarity_in_list, get_keyword_from_synonyms
from .utils import get_entity_synonyms, get_alias_synonyms

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
charging_station_synonyms = get_alias_synonyms(title="charging_station")["charging_station"]
toll_road_synonyms = get_alias_synonyms(title="toll_road")["toll_road"]
negation_synonyms = get_alias_synonyms(title="negation")["negation"]
toll_free_synonyms = get_alias_synonyms(title="toll_free")["toll_free"]


def process_string(string: str) -> object:
    # analyse preprocessed string
    return get_query(string)


def get_query(string: str) -> object:
    LOGGER.info("Received Request \"%s\"", string)

    default_tokens = nlp_default(string.replace("?", "").replace("!", "").strip() + " ")
    result = Query()

    # tokens of NER model
    ner_tokens = ner_model(string)
    ner_token_count = len(ner_tokens)

    # checks if charging stations are queried
    if check_feature(ner_tokens, "charging_station"):
        result.route_attributes.charging_stations = True

    # checks if toll roads are queried
    if check_feature(ner_tokens, "toll_road"):
        result.route_attributes.toll_road_avoidance = False
    else:
        result.route_attributes.toll_road_avoidance = True

    # extracted parameters of NER model
    token_keywords = []
    for index in range(ner_token_count):
        token = ner_tokens[index]
        # save query object
        if token.ent_type_ == "queryObject":
            query_object = get_keyword_from_synonyms(token.lemma_, "route", query_object_synonyms)
            result.query_object = query_object

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

            param_1, param_2, unit = get_query_parameters(index, ner_tokens)

            token_keywords.append(TokenKeywords(token, param_1, param_2, number, unit))

    # set default value
    if result.query_object == "":
        result.query_object = "route"
    
    if result.query_object == "route":
            query_tokens = []
            locations={}
            # adds query tokens to query_tokens and replaces tokens labeled as location with default string "location" 
            for index in range(len(default_tokens)):
                token = default_tokens[index]
                if token.ent_type_ == "LOC":
                    query_tokens.append("location")
                    
                    #saves index of location token 
                    locations[index] = token.lemma_
                else:
                    query_tokens.append(token.text)
            
            # composes the array of tokens to a string
            query = ' '.join(map(str, query_tokens))
            location_tokens = ner_model(query)
            
            # saves the token to the corresponding attribute in query object 
            for index in range(len(location_tokens)):
                token = location_tokens[index]
                if token.ent_type_ == "regionStart":
                    result.route_attributes.location_start = default_tokens[index].lemma_
                elif token.ent_type_ == "regionEnd":
                    result.route_attributes.location_end = default_tokens[index].lemma_
                elif token.ent_type_ == "region":
                    result.route_attributes.location_start = default_tokens[index].lemma_
    else:
        for token in default_tokens:
                if token.ent_type_ == "LOC":
                    result.location = token.lemma_

    return resolve_extracted_query_parameters(result, token_keywords)


def resolve_extracted_query_parameters(result, token_keywords: []) -> object:

    # try to apply all extracted parameters to query object
    unresolved_keywords = apply_query_parameters(token_keywords, result)

    # no need for further postprocessing if all query parameters could be applied to query object
    if len(unresolved_keywords) == 0:
        return result

    # resolve parameters which could not be extracted earlier
    count = len(token_keywords)
    for i in range(count):
        keyword = token_keywords[i]

        # skip keyword analysis if it was resolved
        if keyword not in unresolved_keywords:
            continue

        # the algorithm only knows how to extract additional attributes from adjacent results.
        # If the attribute is known no further information can be extracted

        if keyword.attribute is not None and keyword.unit is not None:
            continue

        # if a previous element exists
        if i != 0:
            prev_keyword = token_keywords[i - 1]
            # check if they are related to each other
            if prev_keyword.min_or_max != keyword.min_or_max and prev_keyword.min_or_max is not None:
                # if they are, add missing information
                add_keyword_information(prev_keyword, keyword)
                continue

        # if a next element exists
        if i + 1 < count:
            next_keyword = token_keywords[i + 1]
            # check if they are related to each other
            if next_keyword.min_or_max != keyword.min_or_max and next_keyword.min_or_max is not None:
                # if they are, add missing information
                add_keyword_information(next_keyword, keyword)

    # try to apply unresolved keywords with additional information
    apply_query_parameters(unresolved_keywords, result, False)

    LOGGER.info("Send %s", result)

    return result


def add_keyword_information(source, target):
    """
    Add information from source TokenKeywords to target TokenKeywords
    @param source:
    @param target:
    """
    if target.attribute is None:
        target.attribute = source.attribute
    if target.unit is None:
        target.unit = source.unit


def apply_query_parameters(token_keywords: [], query: object, require_units=True) -> []:
    """
    Given an array of tokenKeywords, this functions tries to apply all of them to a query object
    @param token_keywords: list of tokenKeywords
    @param query: query object
    @param require_units: True if height and length parameters need a specified unit in order to be applied to query object
    @return: array of tokenKeywords containing not enough information to be applied
    """
    unresolved_keywords = []

    # assign parameters which were found
    for keywords in token_keywords:
        param_1 = keywords.min_or_max
        param_2 = keywords.attribute
        number = keywords.number

        if param_2 == "height" and (keywords.unit is not None or not require_units):
            # select min parameter by default
            if param_1 in ["min", None]:
                query.route_attributes.height.min = convert_number_to_meter(keywords.unit, number)
                continue
            if param_1 == "max":
                query.route_attributes.height.max = convert_number_to_meter(keywords.unit, number)
                continue
        if param_2 == "length" and (keywords.unit is not None or not require_units):
            # select min parameter by default
            if param_1 in ["min", None]:
                query.route_attributes.length.min = convert_number_to_meter(keywords.unit, number)
                continue
            if param_1 == "max":
                query.route_attributes.length.max = convert_number_to_meter(keywords.unit, number)
                continue
        if param_2 == "gradiant":
            # select min parameter by default
            if param_1 in ["min", None]:
                query.route_attributes.gradiant.min = number
                continue
            if param_1 == "max":
                query.route_attributes.gradiant.max = number
                continue
        if param_2 == "curves_left":
            if param_1 in ["min", None]:
                query.route_attributes.curves.left.min = number
                continue
            if param_1 == "max":
                query.route_attributes.curves.left.max = number
                continue
        if param_2 == "curves_right":
            if param_1 in ["min", None]:
                query.route_attributes.curves.right.min = number
                continue
            if param_1 == "max":
                query.route_attributes.curves.right.max = number
                continue

        # parameters could not be assigned
        unresolved_keywords.append(keywords)

    if len(unresolved_keywords) != 0:
        print("Failed to resolve", len(unresolved_keywords), "extracted query parameters")

    return unresolved_keywords


def get_query_parameters(origin: int, tokens: [spacy.tokens.token.Token]) -> (str, str, str):
    """
    @param origin token which requires the attribute it is referring to
    @param tokens all tokens of the sentence
    @return query attributes found in sting. Example: min height km (min or max, parameter, unit)
    """

    dependencies, unit = get_token_dependencies(origin, tokens)
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

    print("Dependencies for", tokens[origin], ":", dependencies, "->", param_1, param_2, unit)

    return param_1, param_2, unit


def get_token_dependencies(origin: int, tokens: [spacy.tokens.token.Token], discovery_right: int = 3, discovery_left: int = 3,
                           stop_on_amount: bool = True) -> ([spacy.tokens.token.Token], str):
    """
    Extracts dependencies of a token, starting its search to the left, then to the right
    @param origin: token from which the search is started
    @param tokens: list of tokens containing origin
    @param discovery_right: max search radius to the right
    @param discovery_left: max search radius to the left
    @param stop_on_amount: stop searching in that direction if another token labeled with amount was found
    @return: (list of dependencies, unit found (km, m, %, ...)
    """

    result = []
    unit = None
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

        # try to extract amount unit
        if unit is None:
            found_unit = check_unit(token, "")
            if found_unit != "":
                unit = found_unit

        # if next token is specifying amount
        if token.ent_type_ == "amount":
            # stop search in that direction
            if stop_on_amount:
                break

        result.append(token)

    return result, unit


def check_unit(token: spacy.tokens.token.Token, default_keyword="km") -> str:
    """
    @param token: the token which is checked for a unit
    @param default_keyword: reuturned if no matching keyword was found
    @return unit, if token has a unit, otherwise the default keyword
    """
    return get_keyword_from_synonyms(token.lemma_, default_keyword, unit_synonyms)


def check_feature(tokens: spacy.tokens.doc.Doc, feature="charging_station"):
    """
    :param tokens
    :param feature, specifies the feature that is checked
    :return true, if one token is equal to a feature synonym and it's not negated, otherwise false
    """
    feature_synonyms = []
    # get the specific synonyms list
    if feature == "charging_station":
        feature_synonyms = charging_station_synonyms
    elif feature == "toll_road":
        feature_synonyms = toll_road_synonyms

    # iterate over all tokens and check if feature is present
    for index in range(len(tokens)):
        token = tokens[index]
        normalized_token = normalize_token(token)
        # iterate over all items in the synonym list and check if token is equal to one synonym
        for synonym in feature_synonyms:
            if check_similarity(synonym, normalized_token, threshold=0.85):
                normalized_previous_token = normalize_token(tokens[index - 1])
                if not check_negation(normalized_previous_token):
                    return True
                else:
                    return False

        # check in-token-negation
        if feature == "toll_road" and check_similarity_in_list( normalized_token, toll_free_synonyms, threshold=0.85):
            return False

    if feature == "toll_road":
        return True
    return False


def normalize_token(token: spacy.tokens.token.Token) -> str:
    """
    :param token
    :return token as lemmatized lowercase string value
    """
    return str(token.lemma_).lower()


def check_negation(prev_token: str) -> bool:
    """
    :param prev_token
    :return true, if token is equal to an item from the negation list
    """
    for synonym in negation_synonyms:
        if check_similarity(synonym, prev_token, threshold=0.8):
            return True
    return False


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
    location_start: Optional[str]
    location_end: Optional[str]
    height: Optional[BaseAttributes]
    length: Optional[BaseAttributes]
    gradiant: Optional[GradiantAttributes]
    curves: Optional[Curves]
    charging_stations: bool
    toll_road_avoidance: bool

    def __init__(self):
        self.height = BaseAttributes()
        self.length = BaseAttributes()
        self.gradiant = GradiantAttributes()
        self.curves = Curves()
        self.charging_stations = False
        self.toll_road_avoidance = False
        self.location_start = ""
        self.location_end = ""


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


class TokenKeywords:
    token: spacy.tokens.token.Token
    min_or_max: str
    attribute: str
    number: int  # number found associated wih parameter (example: 100)
    unit: str  # unit found associated with number (example: km, %)

    def __init__(self, token, min_or_max, attribute, number, unit):
        self.token = token
        self.min_or_max = min_or_max
        self.attribute = attribute
        self.number = number
        self.unit = unit


# print(get_query("Finde eine Strecke in Italien mit mindestens 10 meilen länge in einer lage über 1000 mit einem Anteil von 500 kilometer Linkskurven mit einem Anteil von 600m Steigung über 7% auf einer Höhe von maximal 10 km"))
# print(get_query("Plane mir eine Route nach Paris mit einem Anteil von 500 meter Steigung von maximal 7%"))
# print(get_query("Plane mir eine Route nach Paris mit einer länge von mindestens 100 und maximal 1000 km"))
# print(get_query("Plane mir eine Route nach Paris mit einer länge von mindestens 100 km"))
# print(get_query("Finde eine Strecke in Italien mit mindestens 10 meilen länge in einer lage über 1000  mit einem Anteil von 500 kilometer Linkskurven mit einem Anteil von 600m Steigung über 7% auf einer Höhe von maximal 10"))
# print(get_query("Plane mir eine Route nach Paris mit einem Anteil von 500 meter Steigung von maximal 7% mit eine Ladestationen") )
print(get_query("Zeige mir Berge in Hamburg mit einer Höhe von 1 kilometern"))



