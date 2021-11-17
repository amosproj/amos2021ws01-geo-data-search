FROM python:3.9-slim
WORKDIR /src
COPY . /src
RUN pip install -r requirements.txt
RUN python -m spacy download de_core_news_sm
