import logging
import os
import pathlib

from fastapi import FastAPI
from fastapi.responses import HTMLResponse

from .string_interpreter import process_string, Query

# Logging
logger = logging.getLogger()
logging.basicConfig(level=logging.INFO)

CURRENT_DIR = pathlib.Path(__file__).parent.resolve()

# get os specific file separator
SEP = os.path.sep

# App title and landing page
APP_TITLE = "API for NLP Component"
app = FastAPI(title=APP_TITLE)


@app.on_event("startup")
async def startup_event():
    model_path = pathlib.Path(str(CURRENT_DIR.parent) + f"{SEP}models{SEP}training")
    if model_path.exists():
        logging.info(f"[NLP COMPONENT][API] Found model on path {model_path}")
    else:
        logging.info(f"[NLP COMPONENT][API] No model found on path {model_path}")

        # check whether there are training data available
        data_path = pathlib.Path(str(CURRENT_DIR.parent) + f"{SEP}models{SEP}data{SEP}output{SEP}train{SEP}output.json")
        if data_path.exists():
            # no training data generation needed
            logging.info(f"[NLP COMPONENT][API] Found training data {data_path}")

        else:
            logging.info(f"[NLP COMPONENT][API] Could not find training data file {data_path}")
            # TODO generate training data
        # TODO train model
    # TODO maybe load model


@app.get("/", include_in_schema=False, response_class=HTMLResponse)
async def root():
    return f"<html><head><title>{APP_TITLE}</title></head><body>" \
           f"Welcome! Open /docs to see API documentation.</body></html>"


@app.get(
    path="/version",
    tags=["Version Number"]
)
async def get_version():
    logging.info("[NLP Component] Requested current version number")
    return {"version": "0.0.1"}


@app.get(
    path="/request/{text}",
    tags=["Request NLP Component"],
    response_model=Query
)
async def request(text: str):
    logging.info(f"[NLP Component] Received Request: {text}")
    text = "Finde alle Berge in Berlin die höher als 100m sind"  # TODO remove

    return process_string(text)
