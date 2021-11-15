import json


def get_queries_from_chattete_output(file):
    queries = []
    with open(file) as json_file:
        data = json.load(json_file)
        queryObjects = data["rasa_nlu_data"]["common_examples"]
        for queryObject in queryObjects:
            queries.append(queryObject["text"])
        print(queries)


get_queries_from_chattete_output("./output/train/output.json")
