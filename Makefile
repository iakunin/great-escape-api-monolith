.PHONY: .gradle .idea .jhipster build gradle src

run-db:
	docker-compose -f src/main/docker/postgresql.yml up

stop-db:
	docker-compose -f src/main/docker/postgresql.yml down

run:
	./gradlew

# @TODO: test running migrations
migrate:
	bash bin/migrate.sh

prod-db:
	cloud_sql_proxy -instances=great-escape-294716:europe-west1:api-monolith=tcp:54322
