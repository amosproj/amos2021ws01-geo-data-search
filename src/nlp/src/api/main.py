import logging
logging.basicConfig(format='%(asctime)s %(levelname)s %(name)s: %(message)s',
                    datefmt='%d.%m.%Y %H:%M:%S',
                    encoding='utf-8',
                    level=logging.INFO)
LOGGER = logging.getLogger("src.api.main")

import pathlib
import os


from datetime import datetime
from fastapi import FastAPI
from fastapi.responses import HTMLResponse

from .string_interpreter import process_string, Query

# File Separator
SEP = os.path.sep

CURRENT_DIR = pathlib.Path(__file__).parent.resolve()


# App title and landing page
APP_TITLE = "API for NLP Component"
app = FastAPI(title=APP_TITLE)


def write_log_to_file(text):
    log_dir = pathlib.Path(f'{SEP}tmp{SEP}logs{SEP}')
    file_path = f"{log_dir}{SEP}log_{datetime.now().date()}"
    with open(file_path, "a") as logfile:
        logfile.write(f"{datetime.now()} {text}\n")


@app.get("/", include_in_schema=False, response_class=HTMLResponse)
async def root():
    return f"<html><head><title>{APP_TITLE}</title></head><body>" \
           f"Welcome! Open /docs to see API documentation.</body></html>"


@app.get(
    path="/version",
    tags=["Version Number"]
)
async def get_version():
    logging.info("Requested current version number")
    return {"version": "0.12.0"}


@app.get(
    path="/request/{text}",
    tags=["Request NLP Component"],
    response_model=Query
)
async def request(text: str):
    write_log_to_file(f'Received request \"{text}\"')
    LOGGER.info("Received request \"%s\"", text)

    answer = process_string(text)
    write_log_to_file(f'Send \"{answer}\"')
    LOGGER.info("Send %s", answer)
    return answer
