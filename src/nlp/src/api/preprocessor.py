import spacy
import de_core_news_md

nlp = de_core_news_md.load()

def preprocess_string(string: str) -> str:
    tokens = nlp(string)

    result = ""
    for token in tokens:
        result += token.lemma_ + " "

    return result