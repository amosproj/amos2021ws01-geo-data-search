FROM python:alpine
WORKDIR /src
COPY . /src
ENTRYPOINT ["python3", "./src/main.py", "./src/tests/string_interpreter.py"]
RUN pip install -r requirements.txt


