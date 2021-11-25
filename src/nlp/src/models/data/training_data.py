import os
import pickle
from pathlib import Path

from spacy_formatter import format_chatette_output

# run Chatette
os.system("python -m chatette chatette-test-01.chatette")
SEP = os.path.sep
PATH_SPACY = "output" + SEP


def parse_data(name: str):
    path_nlu = f"output{SEP}{name}{SEP}"

    # ensure that output path exists
    Path(path_nlu).mkdir(parents=True, exist_ok=True)

    # convert rasa output to a more handy one
    files = os.listdir(path_nlu)
    training_data = []
    for file in files:
        sub_training_data = format_chatette_output(path_nlu + file)
        training_data = training_data + sub_training_data
        # write resulting json to file

    pickle.dump(training_data, open(PATH_SPACY + name + "_" + file, "wb"))


# parse training and test data
parse_data("train")
parse_data("test")
