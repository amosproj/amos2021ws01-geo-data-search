import json


def format_chatette_output(file):
    training_data = []
    with open(file) as json_file:
        data = json.load(json_file)
        query_objects = data["rasa_nlu_data"]["common_examples"]
        for query_object in query_objects:
            entities = []
            for entity in query_object["entities"]:
                entities.append((entity["start"], entity["end"], entity["entity"]))
            data_object = (query_object["text"], {"entities": entities})
            training_data.append(data_object)
    return training_data
