# KawaiiBot

## Installation

If you need help with theses steps, you can ask for help on our [discord](https://discord.gg/wGwgWJW)

### Building on the host
```bash
chmod +x gradlew && ./gradlew shadowJar
```

### Building on windows
```bash
./gradlew.bat build
./gradlew.bat shadowJar
```


### Building via Docker
```bash
docker build -t kawaiibot .
```

### Deployment via Docker Compose
```bash
docker-compose up -d --build
```

### Development setup
```bash
chmod +x gradlew && ./gradlew cleanIdea idea
```

