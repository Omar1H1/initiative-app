.PHONY: run runnospring spring react mongo postgres

run:
	kitty -e bash -c "$(MAKE) postgres; exec bash" & \
	kitty -e bash -c "$(MAKE) spring; exec bash" & \
	kitty -e bash -c "$(MAKE) react; exec bash" & \
	kitty -e bash -c "$(MAKE) mongo; exec bash"

runnospring:
	kitty -e bash -c "$(MAKE) postgres; exec bash" & \
	kitty -e bash -c "$(MAKE) react; exec bash" & \
	kitty -e bash -c "$(MAKE) mongo; exec bash"

spring:
	mvn spring-boot:run -U

react:
	cd FrontEnd && npm run dev

mongo:
	docker rm -f mongo || true
	docker run -d --name mongo -p 27017:27017 mongo:latest --replSet rs0
	sleep 5
	docker exec -it mongo mongosh --eval 'rs.initiate({_id: "rs0", members: [{_id: 0, host:"localhost:27017"}]})'

postgres:
	docker rm -f postgres || true
	docker run -d --name postgres -p 5435:5432 \
		-e POSTGRES_DB=app \
		-e POSTGRES_USER=postgres \
		-e POSTGRES_PASSWORD=postgres \
		postgres:latest
	sleep 5
	docker exec -it postgres psql -U postgres -d app -c 'CREATE DATABASE app;' || true
