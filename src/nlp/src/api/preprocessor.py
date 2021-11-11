import spacy

nlp = spacy.load("en_core_web_md")

def preprocess_string(string: str) -> str:
    return string