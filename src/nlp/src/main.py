import subprocess

from models.data.training_data import generate_data
from models.train_model import train

# generate training data with Chatette
generate_data("./models/data")

# train model with spaCy
train(train_data_path="./models/data/output/train_output.json", model_path="./models/training")

# for host machine in order to be able to edit generated files
subprocess.call(['chmod', '-R', '777', './models/data/output'])
subprocess.call(['chmod', '-R', '777', './models/training'])
