# Geo Data Search (AMOS WS 2021/22)

#### Running on a local machine
- install docker: https://docs.docker.com/get-docker/
- install docker-compose: https://docs.docker.com/compose/install/
- From this directory, run in your command line: `docker-compose up -d`
- The service will be available under http://localhost:8080

#### Starting a single container
- `docker compose up backend` - for Backend
- `docker compose up frontend` - for Frontend
- `docker compose up nlp` - for NLP

#### Running example commands inside a container
- `docker-compose run --rm backend <COMMAND>`
- `docker-compose run --rm backend pwd`
- `docker-compose run --rm backend java main/HelloWorldMain.java`
