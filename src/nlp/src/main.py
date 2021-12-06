import logging
import os
import pathlib

from models.data.training_data import generate_data
from models.train_model import train

# get os specific file separator
SEP = os.path.sep

CURRENT_DIR = pathlib.Path(__file__).parent.resolve()

# check if model already exists
model_path = pathlib.Path(str(CURRENT_DIR) + f"{SEP}models{SEP}training")
if model_path.exists():
    logging.info(f"[NLP COMPONENT] Found model on path {model_path}")
else:
    logging.info(f"[NLP COMPONENT] No model found on path {model_path}")

    # check whether there are training data available
    data_path = pathlib.Path(str(CURRENT_DIR) + f"{SEP}models{SEP}data{SEP}output{SEP}train_output.json")
    if data_path.exists():
        # no training data generation needed
        logging.info(f"[NLP COMPONENT] Found training data {data_path}")

    else:
        logging.info(f"[NLP COMPONENT] Could not find training data file {data_path}")

        path = f'{CURRENT_DIR}{SEP}models{SEP}data{SEP}output'
        pathlib.Path(path).mkdir(parents=True, exist_ok=True)
        # for host machine in order to be able to edit generated files, for docker to create files
        os.chmod(path, 0o777)

        # generate training data with Chatette
        generate_data(str(CURRENT_DIR) + f"{SEP}models{SEP}data")

    path = f'{CURRENT_DIR}{SEP}models{SEP}training{SEP}'
    pathlib.Path(path).mkdir(parents=True, exist_ok=True)
    # for host machine in order to be able to edit generated files, for docker to create files
    os.chmod(path, 0o777)

    # train model with spaCy
    train(train_data_path=str(data_path),
          model_path=str(model_path))
