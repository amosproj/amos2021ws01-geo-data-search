FROM python:alpine
WORKDIR /src
COPY . /src
RUN pip install -r requirements.txt

ENTRYPOINT ["uvicorn", "src.api.main:app", "--host", "0.0.0.0", "--port", "8000"]
