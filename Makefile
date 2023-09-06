ifndef BRANCH_NAME
	BRANCH_NAME = $(shell git rev-parse --abbrev-ref HEAD | tr '[:upper:]' '[:lower:]' | tr -d '/')
endif

ifndef UID
	UID = $(shell id -u)
endif
export UID

ifndef GID
	GID = $(shell id -g)
endif
export GID

include .env

COMPOSE_CMD = docker-compose -p $(BRANCH_NAME)
SBT_MEM = 2048
SBT_RUN_OPTS = -mem $(SBT_MEM)
SBT_CMD = $(COMPOSE_CMD) -f cicd/docker-compose.dev.yml run --rm --remove-orphans sbt $(SBT_RUN_OPTS)

sbt: start_dependencies
	$(SBT_CMD) $(CMD)

clean:
	$(SBT_CMD) clean

compile:
	$(SBT_CMD) compile Test/compile

checkFormat:
	$(SBT_CMD) scalafmtCheckAll scalafmtSbtCheck

format:
	$(SBT_CMD) scalafmt Test/scalafmt scalafmtSbt

start_run: start_dependencies
	$(COMPOSE_CMD) -f cicd/docker-compose.run.yml up -d -V

stop_run: stop_dependencies
	$(COMPOSE_CMD) -f cicd/docker-compose.run.yml down -v

start_dependencies:
	$(COMPOSE_CMD) -f cicd/docker-compose.deps.yml up -d -V

stop_dependencies:
	$(COMPOSE_CMD) -f cicd/docker-compose.deps.yml down -v

test:
	$(COMPOSE_CMD) -f cicd/docker-compose.dev.yml run --rm --entrypoint "sbt coverage test coverageReport $(SBT_RUN_OPTS)" sbt

test-only:
	$(SBT_CMD) "testOnly $(TEST_CLASS)"

stop_all: stop_run stop_dependencies
	docker container prune -f
	docker volume prune -f
	docker network prune -f
