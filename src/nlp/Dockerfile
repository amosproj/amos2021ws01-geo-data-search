FROM python:3.9 AS builder

RUN pip install --upgrade pip
RUN python3 -m venv /opt/venv
ENV PATH="/opt/venv/bin:$PATH"

COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt
RUN python -m spacy download de_core_news_sm
RUN echo "/app/src" > /opt/venv/lib/python3.9/site-packages/easy-install.pth
RUN echo "/app/src" > /opt/venv/lib/python3.9/site-packages/nameless.egg-link

FROM python:3.9 AS runner
COPY --from=builder /opt/venv /opt/venv
ENV PYTHONPATH=/app:/opt/venv
ENV PATH=/opt/venv/bin:$PATH
WORKDIR /app

FROM runner AS deployment
COPY entrypoint.sh /app
COPY src /app/src
USER 1000
RUN mkdir -p /tmp/logs
RUN chmod 0777 /tmp/logs
