# <img height="25" src="./images/AILLogoSmall.png" width="40"/> udf-service

<a href="https://www.legosoft.com.mx"><img height="150px" src="./images/Icon.jpeg" alt="AI Legorreta" align="left"/></a>
Microservice that stores and queries for "User Defined Functions" In the futures we will call the a UDFs.

The `udf-service` stores all UDFs in a Postgres database. A UDF is a function defined by a `power-user` in a Phyton
Blockly interface. The UDF can be used anywhere in the UX or other place. For example we used in the
`cartera-ui` microservica as an example. This microservice is in charge for UPDATE/ADD & DELETE operations
are done directly called by the UI microservice `udf-ui`. The access to Postgress is using the imperative
driver, sinve the databas schema utilize many relationships (see JPA in RDBC).

note: this microservice works in conjunction with `udf-ui` and `udf-run` microservices.


## Introduction

This is a microservice that stores all UDFs in  a Postgres database.
Each UDF cane be defined as follows:

- The `datasource` that is defined for any microservice.
- A mapping variable from de `datasource` and the input parameters for the `udf`.
- The code in `python` language. This code is dynamically defined with the necessity to recompile
any microservice.

The `udf-ui` responsibilities are the following:

- Define a `datasource`, i.e., all its variables, type variables nad default values. The `udf-ui` persists
thhe changes directly.
- Define a UDF and its parameters. Again it does not call `udf-service` to make it persistence, but 
but itself to maintain simplicity.
- Capture using `Blockly` UX library to input the `Phyton` code make it
persistence.

The `udf-run` responsibilities are:

- Call `udf-service` when it is going to be executed by a microservice (keep the cache).
- Receive the `parameters` . note: the microservice that needs to execute the UDF is the one who
makes the mapping.
- Executes the UDf and returns the result.

## Examples

The examples are defined in `cartera` microservices as follows:

- Two UDFs are defines on for qualification of an instrument and other to add a field in a forms.
- `cartera-ui` is the microservice who executes both UDFs: one in the portafolio grid and seconf UDF
in the form for details.
- The name os the UDFs are: `Calificacion Actinver` and `monto extra`.

### Send events

No special events are sent via  **Kafka**, just the auditory events (i.e., readed by `audit-service`).


### System parameters database

All parameters are stored in udfDB in Postgres.

No separate Postgres image is created for paramDB:

* A script called 01_init.sh is run when the image is created and creates the udfDB with a new user.
* The microservice uses Flyway to create the tables. The scrip is stored in /resource/db/migration

### Params queries from PostgresDB

This microservice utilizes Spring Data as method to be access.

This repository is just for query the UDF from any micro.service. Therefore it just have one 
REST service.

note: This repository is NOT utilized by the `udf-ui` microservice in order to avoid develop
many REST services that are utilized only by `udf-ui`.

The REST service is:

- Query all existing UDFs define with its mapping from a microservice and a `datasource`.

For more documentation, see de UDFs user documentation.

### Flyway

note: This docker algo generated the PosgresDB, udfDB.

Or a better method is to use the docker dash board to start and stop the application.

```
  IF EXISTS (SELECT FROM pg_database WHERE datname = 'udfdb') THEN
      CREATE USER $APP_DB_USER WITH PASSWORD '$APP_DB_PASS';
      GRANT ALL PRIVILEGES ON DATABASE $APP_DB_NAME TO $APP_DB_USER;
      RAISE NOTICE 'Database already exists';  -- optional
  ELSE
    CREATE USER $APP_DB_USER WITH PASSWORD '$APP_DB_PASS';
    CREATE DATABASE $APP_DB_NAME;
    GRANT ALL PRIVILEGES ON DATABASE $APP_DB_NAME TO $APP_DB_USER;
    \connect $APP_DB_NAME $APP_DB_USER
  END IF;
```

Original
```
#!/bin/bash
set -e
export PGPASSWORD=$POSTGRES_PASSWORD;
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
  CREATE USER $APP_DB_USER WITH PASSWORD '$APP_DB_PASS';
  CREATE DATABASE $APP_DB_NAME;
  GRANT ALL PRIVILEGES ON DATABASE $APP_DB_NAME TO $APP_DB_USER;
  \connect $APP_DB_NAME $APP_DB_USER
  BEGIN;

  COMMIT;
EOSQL
```


### Create the image manually

```
./gradlew bootBuildImage
```

### Publish the image to GitHub manually

```
./gradlew bootBuildImage \
   --imageName ghcr.io/rlegorreta/udf-service \
   --publishImage \
   -PregistryUrl=ghcr.io \
   -PregistryUsername=rlegorreta \
   -PregistryToken=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
```

### Publish the image to GitHub from the IntelliJ

To publish the image to GitHub from the IDE IntelliJ a file inside the directory `.github/workflows/commit-stage.yml`
was created.

To validate the manifest file for kubernetes run the following command:

```
kubeval --strict -d k8s
```

This file compiles de project, test it (for this project is disabled for some bug), test vulnerabilities running
skype, commits the code, sends a report of vulnerabilities, creates the image and lastly push the container image.

<img height="340" src="./images/commit-stage.png" width="550"/>

For detail information see `.github/workflows/commit-stage.yml` file.


### Run the image inside the Docker desktop

```
docker run \
    --net ailegorretaNet \
    -p 8350:8350 \
    -e SPRING_PROFILES_ACTIVE=local \
    udf-service
```

Or a better method use the `docker-compose` tool. Go to the directory `ailegorreta-deployment/docker-platform` and run
the command:

```
docker-compose up
```

## Run inside Kubernetes

### Manually

If we do not use the `Tilt`tool nd want to do it manually, first we need to create the image:

Fist step:

```
./gradlew bootBuildImage
```

Second step:

Then we have to load the image inside the minikube executing the command:

```
image load ailegorreta/udf-service --profile ailegorreta 
```

To verify that the image has been loaded we can execute the command that lists all minikube images:

```
kubectl get pods --all-namespaces -o jsonpath="{..image}" | tr -s '[[:space:]]' '\n' | sort | uniq -c\n
```

Third step:

Then execute the deployment defined in the file `k8s/deployment.yml` with the command:

```
kubectl apply -f k8s/deployment.yml
```

And after the deployment can be deleted executing:

```
kubectl apply -f k8s/deployment.yml
```

Fourth step:

For service discovery we need to create a service applying with the file: `k8s/service.yml` executing the command:

```
kubectl apply -f k8s/service.yml
```

And after the process we can delete the service executing:

```
kubectl deltete -f k8s/service.yml
```

Fifth step:

If we want to use the project outside kubernetes we have to forward the port as follows:

```
kubectl port-forward service/udf-service 8200:80
```

Appendix:

If we want to see the logs for this `pod` we can execute the following command:

```
kubectl logs deployment/udf-service
```

### Using Tilt tool

To avoid all these boilerplate steps is much better and faster to use the `Tilt` tool as follows: first create see the
file located in the root directory of the proyect called `TiltFile`. This file has the content:

```
# Tilt file for udf-service
# Build
custom_build(
    # Name of the container image
    ref = 'udf-service',
    # Command to build the container image
    command = './gradlew bootBuildImage --imageName $EXPECTED_REF',
    # Files to watch that trigger a new build
    deps = ['build.gradle', 'src']
)

# Deploy
k8s_yaml(['k8s/deployment.yml', 'k8s/service.yml'])

# Manage
k8s_resource('param-service', port_forwards=['8350'])
```

To execute all five steps manually we just need to execute the command:

```
tilt up
```

In order to see the log of the deployment process please visit the following URL:

```
http://localhost:10350
```

Or execute outside Tilt the command:

```
kubectl logs deployment/udf-service
```

In order to undeploy everything just execute the command:

```
tilt down
```

To run inside a docker desktop the microservice need to use http://udf-service:8200 path


### Reference Documentation
This microservice uses the recent Spring Gateway :

* [Spring Boot Gateway](https://cloud.spring.io/spring-cloud-gateway/reference/html/)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.0.1/maven-plugin/reference/html/)
* [Config Client Quick Start](https://docs.spring.io/spring-cloud-config/docs/current/reference/html/#_client_side_usage)
* [Spring Boot Actuator](https://docs.spring.io/spring-boot/docs/3.0.1/reference/htmlsingle/#production-ready)

### Links to Springboot 3 Observability

https://tanzu.vmware.com/developer/guides/observability-reactive-spring-boot-3/

Baeldung:

https://www.baeldung.com/spring-boot-3-observability



### Contact AI Legorreta

Feel free to reach out to AI Legorreta on [web page](https://legosoft.com.mx).


Version: 2.0.0
©LegoSoft Soluciones, S.C., 2023
