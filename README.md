# IP Org Resolver

An [application continuum](https://www.appcontinuum.io/) style example using Kotlin and Ktor
that includes a single web application with two background workers.

* Basic web application
* Data analyzer
* Data collector

### Technology stack

This codebase is written in a language called [Kotlin](https://kotlinlang.org) that is able to run on the JVM with full
Java compatibility.
It uses the [Ktor](https://ktor.io) web framework, and runs on the [Netty](https://netty.io/) web server.
HTML templates are written using [Freemarker](https://freemarker.apache.org).
The codebase is tested with [JUnit](https://junit.org/) and uses [Gradle](https://gradle.org) to build a jarfile.
The [pack cli](https://buildpacks.io/docs/tools/pack/) is used to build a [Docker](https://www.docker.com/) container 
which is deployed to:
[Heroku Cloud](https://dashboard.heroku.com) on Heroku's Cloud Platform.
Continuous Integration - GitHub Actions
Continuous Deployment - Heroku Pipeline


## Getting Started

Building a Docker container and running with Docker.

## Buildpacks

1.  Install the [pack](https://buildpacks.io/docs/tools/pack/) CLI.
    ```bash
    brew install buildpacks/tap/pack
    ```

1.  Build using pack.
    ```bash
    pack build ip-org-resolver --builder heroku/buildpacks:20
    ```

1.  Run with docker.
    ```bash
    docker run  -e "PORT=8882" -e "APP=applications/basic-server/build/libs/basic-server-1.0-SNAPSHOT.jar" ip-org-resolver
    ```

## Development

1.  Build a Java Archive (jar) file.
    ```bash
    ./gradlew clean build
    ```

1.  Configure the port that each server runs on.
    ```bash
    export PORT=8881
    ```

Run the servers locally using the below examples.

### Web application

```bash
java -jar applications/basic-server/build/libs/basic-server-1.0-SNAPSHOT.jar
```

### Data collector

```bash
java -jar applications/data-collector-server/build/libs/data-collector-server-1.0-SNAPSHOT.jar
```

### Data analyzer

```bash
java -jar applications/data-analyzer-server/build/libs/data-analyzer-server-1.0-SNAPSHOT.jar
```
## Testing
run:
./gradlew tests
### Unit Tests:
1.  components/workflow-support/src/test/java/WhoisTest.java
2. applications/basic-server/src/test/kotlin/test/collective/start/AppTest.kt

### Integration Test:
1.  applications/basic-server/src/test/kotlin/test/collective/start/AppTest.kt (fun testPostValidIPAddressFormat())
