import itertools
import logging
import os
import pathlib
import re

from typing import Optional

# get os specific file separator
SEP = os.path.sep

CURRENT_DIR = pathlib.Path(__file__).parent.resolve()


def get_synonyms(chatette_file_path: str = None, entity="queryObject") -> dict:
    """
    Extracts all synonyms for the given entity if sub entities are defined
    :param chatette_file_path Path to Chatette file containing the synonym definitions
    :param entity the entity to be parsed, default value is queryObject
    :return Dictionary with sub entity title as key and list with their synonyms as value
    """
    # read synonyms from chatette file
    if chatette_file_path is None:
        chatette_file_path = str(CURRENT_DIR) + f"{SEP}..{SEP}models{SEP}data{SEP}chatette-slots{SEP}{entity}.chatette"
    chatette_file_path = pathlib.Path(chatette_file_path)
    if not chatette_file_path.exists():
        logging.error(f"Couldn't find file {chatette_file_path}")

    with open(chatette_file_path) as file:
        synonyms = {}
        key: Optional[str] = None
        values = []
        optional_alias = re.compile(r"\[[a-z]+\?]")
        for line in file:

            # line is a comment or empty and should be ignored
            if line.strip().startswith("//") or len(line.strip()) == 0:
                continue

            # rm inline comments
            line = line.split("//")[0]

            # line contains (sub-) entity definition
            if "@" in line and "queryObject" in line:
                # add synonyms to dictionary if new sub entity is found
                if key is not None:
                    synonyms[key] = values
                    values = []

                # rm syntax specific chars
                key = line.replace("@[", "") \
                    .replace("&", "") \
                    .replace(f"{entity}#", "") \
                    .replace("]", "") \
                    .replace("\n", "") \
                    .lower() \
                    .strip()

            # found synonym
            if line.startswith(4 * " ") or line.startswith("\t"):
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
                        if sub_part.isalpha():
                            values.append(sub_part.lower().strip())
                            logging.debug(f"[NLP COMPONENT] Added {sub_part} as synonym for {key}")
    synonyms[key] = values
    return synonyms
