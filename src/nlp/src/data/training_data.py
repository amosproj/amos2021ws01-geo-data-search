import os, json, codecs
from spacy_formatter import format_chattete_output


path_nlu = "output/train/"
path_spacy = "output/"

os.system("python -m chatette chatette-test-01.chatette")
files = os.listdir(path_nlu)
training_data = []
for file in files:
    sub_training_data = format_chattete_output(path_nlu + file)
    with open(path_spacy + "spacy" + file, "wb") as f:
        json.dump(sub_training_data, codecs.getwriter("utf-8")(f), ensure_ascii=False)
