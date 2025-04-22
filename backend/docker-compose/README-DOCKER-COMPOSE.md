# JHipster generated Docker-Compose configuration

## Usage

Launch all your infrastructure by running: `docker compose up -d`.

## Configured Docker services

### Service registry and configuration server:

- [Consul](http://localhost:8500)

### Applications and dependencies:

- gateway (gateway application)
- gateway's no database
- ms_user (microservice application)
- ms_user's mysql database
- ms_reel (microservice application)
- ms_reel's mysql database
- ms_reel's elasticsearch search engine
- ms_commentlike (microservice application)
- ms_commentlike's mysql database
- ms_notification (microservice application)
- ms_notification's mysql database
- ms_feed (microservice application)
- ms_feed's mysql database
- ms_feed's elasticsearch search engine

### Additional Services:

- Kafka
- [Prometheus server](http://localhost:9090)
- [Prometheus Alertmanager](http://localhost:9093)
- [Grafana](http://localhost:3000)
- [Keycloak server](http://localhost:9080)
