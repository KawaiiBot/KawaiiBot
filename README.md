# KawaiiBot v3.2.1

## Building on the host
`chmod +x gradlew && ./gradlew shadowJar` -> `build/libs`

## Building via Docker
`docker build -t kawaiibot .`

## Deployment via Docker Compose
`docker-compose up -d --build`

## Development setup
`chmod +x gradlew && ./gradlew cleanIdea idea`
