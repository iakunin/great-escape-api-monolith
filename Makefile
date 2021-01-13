.PHONY: .gradle .idea .jhipster build gradle src

run:
	./gradlew

build:
	./gradlew clean build

# @TODO: test running migrations
migrate:
	bash bin/migrate.sh

prod-db:
	cloud_sql_proxy -instances=great-escape-294716:europe-west1:api-monolith=tcp:54322
