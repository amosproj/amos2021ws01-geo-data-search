import pickle
import sys

import spacy
from spacy.training import Example

# try tp load local ml nlp model
try:
    nlp = spacy.load("./training")
except IOError:
    sys.exit("ML model was not trained locally")

examples = []
# load test data
data = pickle.load(open("./data/output/test_output.json", "rb"))

for text, annots in data:
    doc = nlp.make_doc(text)
    examples.append(Example.from_dict(doc, {"entities": list(set(annots["entities"]))}))
print(nlp.evaluate(examples))  # This will provide overall and per entity metrics
