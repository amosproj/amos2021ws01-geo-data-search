import logging
import sys

logging.basicConfig(format='%(asctime)s %(levelname)s %(name)s : %(message)s',
                    datefmt='%d.%m.%Y %H:%M:%S',
                    encoding='utf-8',
                    stream=sys.stdout,
                    level=logging.INFO)
LOGGER = logging.getLogger("[STRING INTERPRETER][UTILS]")

import itertools
import os
import pathlib
import re

from typing import Optional

# get os specific file separator
SEP = os.path.sep

CURRENT_DIR = pathlib.Path(__file__).parent.resolve()


def parse_synonyms(is_entity: bool = True, chatette_file_path: str = None,
                   title: str = "queryObject") -> dict:
    """
    Extracts all synonyms for the given entity or alias defined in chatette_file_path
    :param is_entity True if title is an entity and not an alias
    :param chatette_file_path Path to Chatette file containing the synonym definitions
    :param title of the entity/alias to be parsed, default value is queryObject
    :return Dictionary with (sub) entity title as key and list with their synonyms as value
    """
    operator: str
    if is_entity:
        operator = "@"
    else:
        operator = "~"

    # read synonyms from chatette file
    if chatette_file_path is None:
        default_path = f"{CURRENT_DIR}{SEP}..{SEP}models{SEP}data{SEP}chatette-slots{SEP}{title}.chatette"
        LOGGER.info(f"No Chatette file was specified, trying {default_path}")
        chatette_file_path = default_path

    chatette_file_path = pathlib.Path(chatette_file_path)
    if not chatette_file_path.exists():
        LOGGER.error(f"Couldn't find file {chatette_file_path}")

    with open(chatette_file_path) as file:
        synonyms = {}
        key: Optional[str] = None
        values = []
        optional_alias = re.compile(r"\[[a-z]+\?]")
        wrong_definition = False

        for line in file:

            # line is a comment or empty and should be ignored
            if line.strip().startswith("//") or len(line.strip()) == 0:
                continue

            # rm inline comments
            line = line.split("//")[0]

            # line contains (sub-) entity definition
            if operator in line and title in line:
                wrong_definition = False

                # add synonyms to dictionary if new sub entity is found
                if key is not None:
                    synonyms[key] = values
                    values = []

                # rm syntax specific chars
                key = line
                if "#" in line:
                    key = line.replace(f"{title}#", "")
                key = key.replace(f"{operator}[", "") \
                    .replace("&", "") \
                    .replace("]", "") \
                    .replace("\n", "") \
                    .lower() \
                    .strip()

            # skip lines with other aliases or entities
            elif wrong_definition \
                    or (("@" in line or "~" in line) and title not in line):
                wrong_definition = True
                continue

            # found synonym
            elif line.startswith(4 * " ") or line.startswith("\t"):
                parts = optional_alias.sub("", line) \
                    .replace(4 * " ", "") \
                    .replace("\t", "") \
                    .replace("\n", "") \
                    .replace("]", "") \
                    .split("[")

                for part in parts:
                    # case: only letters in line or in line aliases
                    sub_parts = [part]
                    if "|" in part and "=" in part:
                        tmp = part.split("|")
                        for i in range(len(tmp)):
                            tmp[i] = tmp[i].split("=")
                        sub_parts = list(itertools.chain(*tmp))
                    elif "=" in part:
                        sub_parts = part.split("=")
                    elif "|" in part:
                        sub_parts = part.split("|")

                    for sub_part in sub_parts:
                        if sub_part.strip().isalpha():
                            values.append(sub_part.lower().strip())
                            LOGGER.debug(f"Added {sub_part} as synonym for {key}")
    synonyms[key] = values
    return synonyms


def get_entity_synonyms(chatette_file_path: str = None, entity: str = "queryObject") -> dict:
    """
     Parses entity synonyms from Chatette file
     :param chatette_file_path Path to Chatette file
     :param entity Name of the entity used in the Chatette file
    """
    entity_synonyms = parse_synonyms(
        is_entity=True,
        chatette_file_path=chatette_file_path,
        title=entity)

    try:
        if list(entity_synonyms.keys())[0] != entity:
            return {entity: entity_synonyms}
    except TypeError:
        return entity_synonyms
    return entity_synonyms


def get_alias_synonyms(chatette_file_path: str = None,
                       title: str = "unit") -> dict:
    """
     Parses alias synonyms from Chatette file
     :param chatette_file_path Path to Chatette file
     :param title Name of the alias used in the Chatette file
    """
    if chatette_file_path is None:
        chatette_file_path = f"{CURRENT_DIR}{SEP}..{SEP}models{SEP}data{SEP}chatette-slots{SEP}aliases.chatette"

    aliases = parse_synonyms(
        is_entity=False,
        chatette_file_path=chatette_file_path,
        title=title)

    try:
        if list(aliases.keys())[0] != title:
            return {title: aliases}
    except TypeError:
        return aliases
    return aliases


def get_keyword_from_synonyms(string: str, default_keyword: str, synonyms: dict, log_results=False):
    """
    checks if a word matches to a synonym from a given dictionary
    @param string: token to be allocated to a synonym class
    @param default_keyword: the keyword used in case if no keyword was found
    @param synonyms: dictionary with keywords and their synonyms/aliases
    @param log_results: true if results are supposed to be logged
    @return key synonym if the word is in the synonym list, otherwise default_keyword for the synonym class
    """
    # get keyword
    for keyword in synonyms.keys():
        if string.lower() in synonyms[keyword]:
            if log_results:
                logging.warning(f"[NLP COMPONENT][STRING INTERPRETER] Found matching keyword {keyword} for {string}")
            return keyword

    if log_results:
        logging.warning(
            f"[NLP COMPONENT][STRING INTERPRETER] Couldn't find a matching keyword for {string}, "
            f"using default keyword {default_keyword}"
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
