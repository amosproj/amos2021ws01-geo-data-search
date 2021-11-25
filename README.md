<p align="center">
  <img src="https://raw.githubusercontent.com/amosproj/amos2021ws01-geo-data-search/main/Deliverables/2021-10-27_sprint-01-team-logo.png" width="500"/>
</p>

# Geo Data Search (AMOS WS 2021/22)
## Mission
Our project mission is to achieve an interpretation of buzzword user queries in German through a web interface concerning location, length, height (difference) of public routes, places and regions. Multiple results, routes or places, matching the user query as close as possible will be displayed in a list as well as in a map. The software should be usable from desktop web browsers and be intuitive - an option to get examples for possible inputs in the web interface will be provided nevertheless.


## Build instructions
#### Running on a local machine
- install docker: https://docs.docker.com/get-docker/
- install docker-compose: https://docs.docker.com/compose/install/
- From this directory, run in your command line: `docker-compose up -d`
- The service will be available under http://localhost:8080

#### Starting a single container
- `docker compose up backend` - for Backend
- `docker compose up frontend` - for Frontend
- `docker compose up nlp` - for NLP

#### Restarting the backend container (for development)
- `docker-compose up -d backend --build`

#### Running example commands inside a container
- `docker-compose run --rm backend <COMMAND>`
- `docker-compose run --rm backend pwd`
- `docker-compose run --rm backend java main/HelloWorldMain.java`
- `docker-compose run --rm nlp python3 helloTest.py`
