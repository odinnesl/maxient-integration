<h1 align="center"> Maxient Integration </h1> <br>

<p align="center">
  Service to API Maxient Integration
</p>


## Table of Contents

- [Introduction](#introduction)
- [Features](#features)
- [Requirements](#requirements)
- [Quick Start](#quick-start)
- [Testing](#testing)


## Introduction

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![CircleCI](https://circleci.com/gh/overture-stack/microservice-template-java/tree/master.svg?style=shield)](https://circleci.com/gh/overture-stack/microservice-template-java/tree/master)

The goal of this service is to provide demographic and schedule data feeds to the case management system (Maxient).

## Features

* Retrieval of student demographic and schedule data
* Publication of data through secure endpoint

## Requirements
The service can be run locally or in a docker container, the requirements for each setup are listed below.


### Local
* [Java 23 SDK](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
* [Maven](https://maven.apache.org/download.cgi)

The class edu.nesl.maxient.Main is the main entry point to the service.

It is run as:

` java -jar maxient-integration-master.jar edu.nesl.maxient.Main`

### Docker
* [Docker](https://www.docker.com/get-docker)


### Production

The application has been set up on the chuprodAPI server. 
Currently only admin accounts have the ability to run the service.

SSH keys for the SFTP service have been shared with Maxient. The service is IP restricted.

### Run Local
```bash
$ mvn spring-boot:run
```

### Run Docker

First build the image:
```bash
$ docker-compose build
```

When ready, run it:
```bash
$ docker-compose up
```

Application will run by default on port `1234`

Configure the port by changing `services.api.ports` in __docker-compose.yml__. 
Port 1234 was used by default so the value is easy to identify and change in the configuration file.


## Testing

Application integration tests are located in src/test/IT folder


## Future Considerations
- Use of POJOs for User domain object
- Use of CircleCI for test coverage reporting


