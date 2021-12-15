import logging

from fastapi import FastAPI
from fastapi.responses import HTMLResponse

from .string_interpreter import process_string, Query

# Logging
logger = logging.getLogger()
logging.basicConfig(level=logging.INFO)

# App title and landing page
APP_TITLE = "API for NLP Component"
app = FastAPI(title=APP_TITLE)


@app.get("/", include_in_schema=False, response_class=HTMLResponse)
async def root():
    return f"<html><head><title>{APP_TITLE}</title></head><body>" \
           f"Welcome! Open /docs to see API documentation.</body></html>"


@app.get(
    path="/version",
    tags=["Version Number"]
)
async def get_version():
    logging.warning("[NLP Component] Requested current version number")
    return {"version": "0.8.0"}


@app.get(
    path="/request/{text}",
    tags=["Request NLP Component"],
    response_model=Query
)
async def request(text: str):
    logging.warning(f"[NLP Component] Received Request: {text}")

    answer = process_string(text)
    logging.warning(f"[NLP Component] Answer: {answer}")

    return answer
