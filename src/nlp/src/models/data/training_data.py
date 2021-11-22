import pickle
import os
from pathlib import Path
from spacy_formatter import format_chatette_output, convert_data_into_spacy_format

SEP = os.path.sep
PATH_NLU = f"output{SEP}train{SEP}"
PATH_SPACY = "output" + SEP

# run Chatette
os.system("python -m chatette chatette-test-01.chatette")

# ensure that output path exists
Path(PATH_NLU).mkdir(parents=True, exist_ok=True)

# convert rasa output to a more handy one
files = os.listdir(PATH_NLU)
training_data = []
for file in files:
    sub_training_data = format_chatette_output(PATH_NLU + file)
    training_data = training_data + sub_training_data
    # write resulting json to file

pickle.dump(training_data, open(PATH_SPACY + "spacy" + file, "wb"))
