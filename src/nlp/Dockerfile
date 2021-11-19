FROM python:3.9 AS builder

RUN pip install --upgrade pip
RUN python3 -m venv /opt/venv
ENV PATH="/opt/venv/bin:$PATH"

COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt
RUN python -m spacy download de_core_news_sm

FROM python:3.9 AS runner
COPY --from=builder /opt/venv /opt/venv
ENV PYTHONPATH=/app:/opt/venv
ENV PATH=/opt/venv/bin:$PATH

WORKDIR /src
COPY . .
