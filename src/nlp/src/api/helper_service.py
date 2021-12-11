from pathlib import Path
from spacy import displacy


def convert_number_to_meter(unit: str, number: int) -> str:
    if unit == "m":
        return number
    elif unit == "miles":
        return number * 1609.34
    # assumed default unit is km
    return number * 1000


def save_dependecies_tree(doc):
    output_path = Path("dependencies_tree.svg")
    svg = displacy.render(doc, style="dep")
    with output_path.open("w", encoding="utf-8") as fh:
        fh.write(svg)
