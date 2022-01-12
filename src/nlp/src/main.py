import logging
logging.basicConfig(format='%(asctime)s %(levelname)s : %(name)s : %(message)s',
                    datefmt='%d.%m.%Y %H:%M:%S',
                    encoding='utf-8',
                    level=logging.DEBUG)
LOGGER = logging.getLogger("src.main")

import os
import pathlib
import subprocess

from models.data.training_data import generate_data
from models.train_model import train

# get os specific file separator
SEP = os.path.sep

CURRENT_DIR = pathlib.Path(__file__).parent.resolve()

# check if model already exists
model_path = pathlib.Path(str(CURRENT_DIR) + f"{SEP}models{SEP}training")
if model_path.exists():
    LOGGER.info("Found model on path %s", model_path)
else:
    LOGGER.info("No model found on path %s", model_path)

    # check whether there are training data available
    data_path = pathlib.Path(str(CURRENT_DIR) + f"{SEP}models{SEP}data{SEP}output{SEP}train_output.json")
    if data_path.exists():
        # no training data generation needed
        LOGGER.info("Found training data %s", data_path)

    else:
        LOGGER.info("Could not find training data file %s", data_path)

        # generate training data with Chatette
        generate_data(str(CURRENT_DIR) + f"{SEP}models{SEP}data")

        path = f'{CURRENT_DIR}{SEP}models{SEP}data{SEP}output'
        # for host machine in order to be able to edit generated files, for docker to create files
        if SEP == "/":
            subprocess.call(['chmod', '-R', '777', path])
        else:
            os.chmod(path, 0o777)

    # train model with spaCy
    train(train_data_path=str(data_path),
          model_path=str(model_path))

    # for host machine in order to be able to edit generated files, for docker to create files
    path = f'{CURRENT_DIR}{SEP}models{SEP}training{SEP}'
    pathlib.Path(path).mkdir(parents=True, exist_ok=True)
    if SEP == "/":
        subprocess.call(['chmod', '-R', '777',  path])
    else:
        os.chmod(path, 0o777)

# prepare API log path
log_path = pathlib.Path(f'{CURRENT_DIR}{SEP}api{SEP}logs{SEP}')
if not log_path.exists():
    LOGGER.info("No log path found on path %s", log_path)
    pathlib.Path(log_path).mkdir(parents=True, exist_ok=True)
    if SEP == "/":
        subprocess.call(['chmod', '-R', '777',  log_path])
    else:
        os.chmod(log_path, 0o777)

    LOGGER.info("Created log path %s", log_path)
