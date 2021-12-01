import os
import subprocess

from models.data.training_data import generate_data
from models.train_model import train

# get os specific file separator
SEP = os.path.sep


# generate training data with Chatette
generate_data(f".{SEP}models{SEP}data")

# train model with spaCy
train(train_data_path=f".{SEP}models{SEP}data{SEP}output{SEP}train_output.json",
      model_path=f".{SEP}models{SEP}training")

# for host machine in order to be able to edit generated files
subprocess.call(['chmod', '-R', '777', f'.{SEP}models{SEP}data{SEP}output'])
subprocess.call(['chmod', '-R', '777', f'.{SEP}models{SEP}training'])
