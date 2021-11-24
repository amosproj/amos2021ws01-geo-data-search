import os
import pickle
from pathlib import Path

from spacy_formatter import format_chatette_output

# run Chatette
os.system("python -m chatette chatette-test-01.chatette")
SEP = os.path.sep


def parse_data(name: str):
    PATH_NLU = f"output{SEP}{name}{SEP}"
    PATH_SPACY = "output" + SEP

    # ensure that output path exists
    Path(PATH_NLU).mkdir(parents=True, exist_ok=True)

    # convert rasa output to a more handy one
    files = os.listdir(PATH_NLU)
    training_data = []
    for file in files:
        sub_training_data = format_chatette_output(PATH_NLU + file)
        training_data = training_data + sub_training_data
        # write resulting json to file

    pickle.dump(training_data, open(PATH_SPACY + name + "_" + file, "wb"))


parse_data("train")
parse_data("test")
