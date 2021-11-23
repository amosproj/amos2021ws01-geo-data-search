import spacy


nlp = spacy.load("./training")

doc = nlp("zeige mir 2000 seen in Berlin")
print(nlp.pipe_names)
for ent in doc.ents:
    print(ent.text, ent.start_char, ent.end_char, ent.label_)
