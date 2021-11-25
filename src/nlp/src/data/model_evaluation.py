import spacy

from spacy.training import Example

# load spacy ml nlp model
nlp = spacy.load("en_core_web_sm")

examples = []
data = [("Taj mahal is in Agra.", {"entities": [(0, 9, 'name'), (16, 20, 'place')]})]

for text, annots in data:
    doc = nlp.make_doc(text)
    examples.append(Example.from_dict(doc, annots))
print(nlp.evaluate(examples))  # This will provide overall and per entity metrics
