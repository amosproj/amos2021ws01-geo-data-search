import logging
import sys
logging.basicConfig(format='%(asctime)s %(levelname)s : %(name)s : %(message)s',
                    datefmt='%d.%m.%Y %H:%M:%S',
                    encoding='utf-8',
                    stream=sys.stdout,
                    level=logging.INFO)
LOGGER = logging.getLogger("src.models.train_model")


import pickle
import random

import spacy
from spacy.training.example import Example


def train(train_data_path="./data/output/train_output.json", base_model=spacy.load("de_core_news_sm"), n_iter=5, model_path="./training/"):
    train_data = pickle.load(open(train_data_path, "rb"))
    LOGGER.debug("Loaded dataset from %s", train_data_path)

    ner = base_model.get_pipe("ner")
    for _, annotations in train_data:
        for ent in annotations.get("entities"):
            ner.add_label(ent[2])  # Initializing optimizer if model is None:
    optimizer = base_model.create_optimizer()
    other_pipes = [pipe for pipe in base_model.pipe_names if pipe != "ner"]
    # adding a named entity label
    ner = base_model.get_pipe("ner")
    with base_model.disable_pipes(*other_pipes):
        LOGGER.debug("Start training")
        for itn in range(n_iter):
            random.shuffle(train_data)
            losses = {}

            # batch the examples and iterate over them
            for batch in spacy.util.minibatch(train_data, size=50):
                for text, annotations in batch:
                    # create Example
                    doc = base_model.make_doc(text)
                    example = Example.from_dict(doc, {"entities": list(set(annotations["entities"]))})
                    # Update the model
                    base_model.update([example], losses=losses, drop=0.3)
    LOGGER.debug("Finished training NER model")
    base_model.meta["name"] = "trained_model"  # rename model
    base_model.to_disk(model_path)
    LOGGER.info("Saved trained model to " + model_path)
