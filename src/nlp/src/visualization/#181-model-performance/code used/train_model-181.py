import logging

logger = logging.getLogger("src.models.train_model")
logging.basicConfig(level=logging.INFO)

import pickle
import random
import csv

import spacy
from spacy.training.example import Example


def train(train_data_path="./data/output/train_output.json", base_model=spacy.load("de_core_news_sm"), n_iter=5,
          model_path="./training/"):
    train_data = pickle.load(open(train_data_path, "rb"))
    logger.debug("Loaded dataset from " + train_data_path)

    ner = base_model.get_pipe("ner")
    for _, annotations in train_data:
        for ent in annotations.get("entities"):
            ner.add_label(ent[2])  # Initializing optimizer if model is None:
    optimizer = base_model.create_optimizer()
    other_pipes = [pipe for pipe in base_model.pipe_names if pipe != "ner"]
    # adding a named entity label
    ner = base_model.get_pipe("ner")
    with base_model.disable_pipes(*other_pipes):
        logger.debug("Start training")
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
    logger.debug("Finished training NER model")
    base_model.meta["name"] = "trained_model"  # rename model
    # base_model.to_disk(model_path)
    # logger.info("Saved trained model to " + model_path)

    examples = []
    # load test data
    test_data = pickle.load(open("./data/output/test_output.json", "rb"))

    for text, annots in test_data:
        doc = base_model.make_doc(text)
        examples.append(Example.from_dict(doc, {"entities": list(set(annots["entities"]))}))

    evaluation = base_model.evaluate(examples)

    with open("model_evaluations.csv", "a", newline="") as eval_file:
        eval_writer = csv.writer(eval_file)

        eval_writer.writerow([n_iter,
                              evaluation["ents_p"], evaluation["ents_f"],
                              evaluation["ents_per_type"]["amount"]["p"],
                              evaluation["ents_per_type"]["amount"]["f"],
                              evaluation["ents_per_type"]["queryObject"]["p"],
                              evaluation["ents_per_type"]["queryObject"]["f"],
                              evaluation["ents_per_type"]["region"]["p"],
                              evaluation["ents_per_type"]["region"]["f"],
                              evaluation["ents_per_type"]["regionStart"]["p"],
                              evaluation["ents_per_type"]["regionStart"]["f"],
                              evaluation["ents_per_type"]["regionEnd"]["p"],
                              evaluation["ents_per_type"]["regionEnd"]["f"],
                              ])


with open("model_evaluations.csv", "a", newline="") as file:
    writer = csv.writer(file)

    # write the header
    writer.writerow(["Iterations",
                     "Precision", "F1-Score",
                     "Precision (amount)", "F1-Score (amount)",
                     "Precision (queryObject)", "F1-Score (queryObject)",
                     "Precision (region)", "F1-Score (region)",
                     "Precision (regionStart)", "F1-Score (regionStart)",
                     "Precision (regionEnd)", "F1-Score (regionEnd)"])

for i in list(range(1, 21)) + [30, 40, 50]:
    train(n_iter=i)
    print("Iteration", i, "done")
