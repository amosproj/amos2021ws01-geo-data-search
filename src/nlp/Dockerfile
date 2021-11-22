FROM python:3.9 AS dep_builder

RUN pip install --upgrade pip
RUN python3 -m venv /opt/venv
ENV PATH="/opt/venv/bin:$PATH"

COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt
RUN python -m spacy download de_core_news_sm

FROM python:3.9 AS tests_builder
COPY --from=dep_builder /opt/venv /opt/venv
ENV PYTHONPATH=/app:/opt/venv
ENV PATH=/opt/venv/bin:$PATH
COPY . /app
RUN pip install -e .

FROM python:3.9 AS runner
COPY --from=tests_builder /opt/venv /opt/venv
ENV PYTHONPATH=/app:/opt/venv
ENV PATH=/opt/venv/bin:$PATH
WORKDIR /app
COPY --from=tests_builder /app /app
