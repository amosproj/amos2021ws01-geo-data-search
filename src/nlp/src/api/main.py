from fastapi import FastAPI
from fastapi.responses import HTMLResponse


# App title and landing page

app_title = "API for NLP Component"
app = FastAPI(title=app_title)


@app.get("/", include_in_schema=False, response_class=HTMLResponse)
async def root():
    return f"<html><head><title>{app_title}</title></head><body>" \
           f"Welcome! Open /docs to see API documentation.</body></html>"


@app.get(
    path="/version",
    tags=["Version Number"]
)
async def get_version():
    return "0.0.2"


@app.get(
    path="/request/{text}",
    tags=["Request NLP "]
)
async def request(text: str):
    return text
