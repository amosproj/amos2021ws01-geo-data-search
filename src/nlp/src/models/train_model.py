import pickle
import random

import spacy
from spacy.training.example import Example

TRAIN_DATA_PATH = "./data/output/spacyoutput.json"
TRAIN_DATA = pickle.load(open(TRAIN_DATA_PATH, "rb"))

nlp = spacy.load("de_core_news_sm")
ner = nlp.get_pipe("ner")
n_iter = 1

for _, annotations in TRAIN_DATA:
    for ent in annotations.get("entities"):
        ner.add_label(ent[2])  # Inititalizing optimizer if model is None:

optimizer = nlp.create_optimizer()
other_pipes = [pipe for pipe in nlp.pipe_names if pipe != "ner"]

# adding a named entity label
ner = nlp.get_pipe("ner")

with nlp.disable_pipes(*other_pipes):
    for itn in range(n_iter):
        random.shuffle(TRAIN_DATA)
        losses = {}

        # batch the examples and iterate over them
        for batch in spacy.util.minibatch(TRAIN_DATA, size=50):
            for text, annotations in batch:
                # create Example
                doc = nlp.make_doc(text)
                example = Example.from_dict(doc, annotations)
                # Update the model
                nlp.update([example], losses=losses, drop=0.3)

nlp.meta["name"] = "trained_model"  # rename model
nlp.to_disk("./training/")
