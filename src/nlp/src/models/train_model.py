import logging
import pickle
import random

import spacy
from spacy.training.example import Example

# Logging
logger = logging.getLogger()
logging.basicConfig(level=logging.INFO)


def train(train_data_path="./data/output/train_output.json", base_model=spacy.load("de_core_news_sm"), n_iter=5, model_path="./training/"):
    train_data = pickle.load(open(train_data_path, "rb"))
    logger.debug("[NLP Component][TRAIN MODEL] Loaded dataset from " + train_data_path)

    ner = base_model.get_pipe("ner")
    for _, annotations in train_data:
        for ent in annotations.get("entities"):
            ner.add_label(ent[2])  # Initializing optimizer if model is None:
    optimizer = base_model.create_optimizer()
    other_pipes = [pipe for pipe in base_model.pipe_names if pipe != "ner"]
    # adding a named entity label
    ner = base_model.get_pipe("ner")
    with base_model.disable_pipes(*other_pipes):
        logger.debug("[NLP COMPONENT][TRAIN MODEL] Start training")
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
    logger.debug("[NLP COMPONENT][TRAIN MODEL] Finished training NER model")
    base_model.meta["name"] = "trained_model"  # rename model
    base_model.to_disk(model_path)
    logger.info("[NLP COMPONENT][TRAIN MODEL] Saved trained model to " + model_path)
