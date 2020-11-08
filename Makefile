.PHONY: .gradle .idea .jhipster build gradle src

run-db:
	docker-compose -f src/main/docker/postgresql.yml up

stop-db:
	docker-compose -f src/main/docker/postgresql.yml down

run:
	./gradlew
