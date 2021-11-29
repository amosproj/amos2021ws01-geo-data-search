import logging
import os
import pickle
from pathlib import Path

from .spacy_formatter import format_chatette_output

# Logging
logger = logging.getLogger()
logging.basicConfig(level=logging.INFO)

SEP = os.path.sep


def generate_data(chatette_path="") -> None:
    if chatette_path != "" and chatette_path[-1] != SEP:
        chatette_path += SEP

    # run Chatette
    os.system(f"python -m chatette {chatette_path}chatette-test-01.chatette")

    # bring rasa format into spacy format
    parse_data("train", chatette_path)
    parse_data("test", chatette_path)


def parse_data(name: str, chatette_path="") -> None:
    try:
        path_nlu = f"{chatette_path}output{SEP}{name}{SEP}"
        path_spacy = f"{chatette_path}output{SEP}"

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
            logger.info("[NLP COMPONENT][TRAINING DATA] Generated " + path_spacy + name + "_" + file)

    except FileNotFoundError:
        logger.error(f"[NLP COMPONENT][TRAINING DATA] Could not find directory \"{name}\" in \"{path_spacy}\"")


# parse training and test data
parse_data("train")
parse_data("test")
