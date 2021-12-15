#!/usr/bin/env bash
#generating the training data and model if absent
python src/main.py

#start API
uvicorn src.api.main:app --host 0.0.0.0 --port 8000 --reload
