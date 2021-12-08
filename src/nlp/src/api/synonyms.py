synonyms = {
    "unit": {
        "m": ["meter", "m", "metern"],
        "km": ["kilometer", "km", "kilo", "kilometern"],
        "miles": ["meile", "meilen"],
    }
}


def check_synonym(synonym_class: str, word: str) -> str:
    """
    checks if a word matches to a synonym from a specific class
    :param  synonym_class the class in which the synonyms are searched for
    :param  word
    :return key synonym if the word is in the synonym list, otherwise an empty string
    """
    synonym_class_list = synonyms[synonym_class]
    for synonym in synonym_class_list:
        if word.text.lower() in synonym_class_list[synonym]:
            return synonym
    return "km"
