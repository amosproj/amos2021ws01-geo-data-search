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

## Named Entity Recognition (NER) Model

### Generate Data Set
Run script training_data.py to generate a training data set:

```
$ cd src/models/data/
$ python training_data.py
```
This script generates the training data set in NLU format in the directory data/output/train. In a following step, the generated output will be converted in spacy format and saved in the directory data/output.

### Train NER model 

Run train_model.py to train a NER model:

```
$ cd src/models/
$ python training_data.py
```
In this script, an existing NER model will be trained with training data and thus extended the model with custom labels.
