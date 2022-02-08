import logging
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger("src.models.training_data")

import os
import pickle
from pathlib import Path

from .spacy_formatter import format_chatette_output


SEP = os.path.sep


def generate_data(chatette_path="") -> None:
    output_path = ""
    if chatette_path not in ["", "."] and chatette_path[-1] != SEP:
        chatette_path += SEP
        output_path = f" -o {chatette_path}output{SEP}"

    # run Chatette
    os.system(f"python -m chatette {chatette_path}chatette-test-01.chatette{output_path}")

    # bring rasa format into spacy format
    parse_data("train", chatette_path)
    parse_data("test", chatette_path)


def parse_data(name: str, chatette_path="") -> None:
    path_spacy = f"{chatette_path}output{SEP}"
    try:
        path_nlu = f"{chatette_path}output{SEP}{name}{SEP}"

        # ensure that output path exists
        Path(path_nlu).mkdir(parents=True, exist_ok=True)

        # convert rasa output to a more handy one
        files = os.listdir(path_nlu)
        training_data = []
        for file in files:
            sub_training_data = format_chatette_output(path_nlu + file)
            training_data = training_data + sub_training_data
            # write resulting json to file

            pickle.dump(training_data, open(path_spacy + name + "_" + file, "wb"))
            logger.info("Generated " + path_spacy + name + "_" + file)

    except FileNotFoundError:
        logger.error("Could not find directory \"%s\" in \"%s\"", name, path_spacy)


# parse training and test data
parse_data("train")
parse_data("test")
