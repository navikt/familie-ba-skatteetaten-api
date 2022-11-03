FROM ghcr.io/navikt/baseimages/temurin:17

COPY ./target/familie-ba-skatteetaten-api.jar "app.jar"
