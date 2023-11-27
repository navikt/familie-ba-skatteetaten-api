FROM ghcr.io/navikt/baseimages/temurin:21

COPY ./target/familie-ba-skatteetaten-api.jar "app.jar"
