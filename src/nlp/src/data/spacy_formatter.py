import json


def format_chattete_output(file):
    trainingData = []
    with open(file) as json_file:
        data = json.load(json_file)
        queryObjects = data["rasa_nlu_data"]["common_examples"]
        for queryObject in queryObjects:
            entities = []
            for entity in queryObject["entities"]:
                entities.append((entity["start"], entity["end"], entity["entity"]))
            dataObejct = (queryObject["text"], {"entities": entities})
            trainingData.append(dataObejct)
    return trainingData
