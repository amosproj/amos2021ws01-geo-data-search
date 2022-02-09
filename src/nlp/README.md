# NLP Component

## Folder structure

    ├── ...
    ├── src                    
    │   ├── api                 # Server related files 
    │   ├── models              # NER model related files
    │   │   ├── data            # files for generating data set
    │   │   ├── training        # trained model
    │   │   ├── train_model.py  # script for training model
    │   │
    │   └── visualization       # plotted figures of evaluation     
    ├──  tests                  # test files 
    └── ...

### Run local
`uvicorn main:app` -> use `--reload` flag during development

see http://127.0.0.1:8000/docs
> see http://127.0.0.1:4000/docs instead when using docker

### Logs
Logs are always output to the console. In addition to that there are log files in the container in `/tmp/logs` where all requests received and answers given by the NLP component are stored.

## Named Entity Recognition (NER) Model

The docker container checks whether a model is trained or not and if there is no model, it will be trained by the available training data. If there are no training data, they are automatically generated from the `chatette-test-01.chatette` file.

By executing `src/main.py`, the same effect can be achieved when not using a docker.

In case generating training data and train with them should be done separately, follow the instructions below: 

### Generate Data Set
Run script training_data.py to generate a training data set:

```
$ cd src/models/data/
$ python training_data.py
```
This script generates the training data set in NLU format in the directory data/output/train. In a following step, the generated output will be converted in spacy format and saved in the directory data/output.

### Train NER model 

Add `train()` at the end of train_model.py (without indentation), then run train_model.py to train a NER model:

```
$ cd src/models/
$ python train_model.py
```
In this script, an existing NER model will be trained with training data and thus extended the model with custom labels.
