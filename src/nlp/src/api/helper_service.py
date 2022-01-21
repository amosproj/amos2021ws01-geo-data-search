import logging
import sys

logging.basicConfig(format='%(asctime)s %(levelname)s %(name)s : %(message)s',
                    datefmt='%d.%m.%Y %H:%M:%S',
                    encoding='utf-8',
                    stream=sys.stdout,
                    level=logging.INFO)
LOGGER = logging.getLogger("[STRING INTERPRETER][HELPER SERVICE]")

import numpy as np
from array import array

from api.utils import get_entity_synonyms, get_alias_synonyms


def convert_number_to_meter(unit: str, number: int) -> int:
    """
    Converts input number into meter
    :param unit Unit of number
    :param number Integer to be converted
    """

    # meters is default unit
    if unit == "m":
        return number

    # convert miles to meters
    if unit == "miles":
        return int(number * 1609.34)

    # convert km to meters
    if unit == "km":
        return number * 1000

    return number


def check_similarity_in_list(token_1: str, token_list: array, threshold: int) -> bool:
    """
    :param token_1
    :param token_list
    :param threshold
    :return true, if token_1 and at least one token in token_list are equal due to levenshtein distance and a given threshold, otherwise false
    """

    for token in token_list:
        if check_similarity(token_1, token, threshold):
            return True
    return False


def check_similarity(token_1: str, token_2: str, threshold: int) -> bool:
    """
    :param token_1
    :param token_2
    :param threshold
    :return true, if tokens are equal due to levenshtein distance and a given threshold, otherwise false
    """

    distance_ratio = compute_levenshtein_distance(token_1, token_2)
    if distance_ratio > threshold:
        return True
    else:
        return False


def compute_levenshtein_distance(token_1: str, token_2: str) -> int:
    """
    Calculates levenshtein distance between two tokens.
    :param token_1
    :param token_2
    :return levenshtein distance ratio
    """
    # initialize matrix
    rows = len(token_1) + 1
    cols = len(token_2) + 1
    distance = np.zeros((rows, cols), dtype=int)

    # calculate matrix with the indeces of each character of both tokens
    for i in range(1, rows):
        for k in range(1, cols):
            distance[i][0] = i
            distance[0][k] = k

    # iterate over the matrix to compute the cost   
    for col in range(1, cols):
        for row in range(1, rows):
            # if the characters are the same in the two strings in a given position [i,j]
            if token_1[row - 1] == token_2[col - 1]:
                cost = 0  # then the cost is 0
            else:
                cost = 1  # otherwise the cost is 1
            distance[row][col] = min(distance[row - 1][col] + 1,  # deletion cost
                                     distance[row][col - 1] + 1,  # insertion cost
                                     distance[row - 1][col - 1] + cost)  # substitution cost
    # compute final Levenshtein Distance Ratio
    distance_ratio = ((len(token_1) + len(token_2)) - distance[row][col]) / (len(token_1) + len(token_2))
    return distance_ratio


def get_keyword_from_synonyms(string: str, default_keyword: str, synonyms: dict):
    """
    checks if a word matches to a synonym from a given dictionary
    :param  string token to be allocated to a synonym class
    :param  default_keyword the keyword used in case if no keyword was found
    :param  synonyms dictionary with keywords and their synonyms/aliases
    :return key synonym if the word is in the synonym list, otherwise default_keyword for the synonym class
    """
    # get keyword
    for keyword in synonyms.keys():
        # iterate over all items in the synonym list and check if token is equal to one synonym
        for synonym in synonyms[keyword]:
            if check_similarity(synonym, string.lower(), threshold=0.85):
                LOGGER.debug("Found matching keyword %s for %s", keyword, string)
                return keyword

    LOGGER.info(
        "Couldn't find a matching keyword for %s, "
        "using default keyword %s",
        string, default_keyword
    )
    return default_keyword


def get_keyword(string: str, default_keyword: str, synonym_class: str, is_entity: bool) -> str:
    """
    checks if a word matches to a synonym from a specific class
    :param  string token to be allocated to a synonym class
    :param  default_keyword the keyword used in case if no keyword was found
    :param  synonym_class the class in which the synonyms are searched for
    :param  is_entity defines whether synonym_class is a named entity
    :return key synonym if the word is in the synonym list, otherwise default_keyword for the synonym class
    """
    if is_entity:
        synonyms = get_entity_synonyms(entity=synonym_class)
    else:
        synonyms = get_alias_synonyms(title=synonym_class)
    return get_keyword_from_synonyms(string, default_keyword, synonyms[synonym_class])
