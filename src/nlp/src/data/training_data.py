import codecs
import json
import os

from pathlib import Path
from spacy_formatter import format_chatette_output

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

    # write resulting json to file
    with open(PATH_SPACY + "spacy" + file, "wb") as f:
        json.dump(sub_training_data, codecs.getwriter("utf-8")(f), ensure_ascii=False)
