import pickle

import spacy
from spacy.training import Example

# load spacy ml nlp model
nlp = spacy.load("./training")  # TODO catch Exception if model isn't trained locally

examples = []
data = pickle.load(open("./data/output/spacyoutput.json", "rb"))  # TODO use test data instead of training

for text, annots in data:
    doc = nlp.make_doc(text)
    examples.append(Example.from_dict(doc, {"entities": list(set(annots["entities"]))}))
print(nlp.evaluate(examples))  # This will provide overall and per entity metrics
