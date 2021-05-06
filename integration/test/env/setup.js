const fs = require('fs');
const os = require('os');
const path = require('path');
const mkdirp = require('mkdirp');
const {DockerComposeEnvironment, Wait} = require("testcontainers");

const DIR = path.join(os.tmpdir(), 'jest_testcontainers_global_setup');
const BUILD_CONTEXT = path.resolve(__dirname, './../../..')
const COMPOSE_FILE = 'docker-compose.yaml'

module.exports = async function () {
    console.log('\nBuilding Docker compose...')
    const dockerComposeEnvironment = await new DockerComposeEnvironment(BUILD_CONTEXT, COMPOSE_FILE)
        .withWaitStrategy('rabbitmq_1', Wait.forHealthCheck())
        .withWaitStrategy('mongo_1', Wait.forHealthCheck())
        .withWaitStrategy('app_1', Wait.forHealthCheck())
        .up();
    console.log('Docker compose is up')

    global.__DOCKER_COMPOSE_ENV__ = dockerComposeEnvironment;

    const rabbitMQ = dockerComposeEnvironment.getContainer('rabbitmq_1');
    const mongo = dockerComposeEnvironment.getContainer('mongo_1');
    const app = dockerComposeEnvironment.getContainer('app_1')

    const appBaseUrl = `http://${app.getHost()}:${app.getMappedPort(8080)}`
    const amqpBaseUrl = `amqp://${rabbitMQ.getHost()}:${rabbitMQ.getMappedPort(5672)}`
    const mongoBaseUrl = `mongodb://${mongo.getHost()}:${mongo.getMappedPort(27017)}/`

    // use the file system to expose the baseUrls for TestEnvironments
    mkdirp.sync(DIR);
    fs.writeFileSync(path.join(DIR, 'appBaseUrl'), appBaseUrl);
    fs.writeFileSync(path.join(DIR, 'amqpBaseUrl'), amqpBaseUrl);
    fs.writeFileSync(path.join(DIR, 'mongoBaseUrl'), mongoBaseUrl);
};
