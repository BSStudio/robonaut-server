const path = require('path')
const {DockerComposeEnvironment, Wait} = require('testcontainers')

function startCompose() {
    const buildContext = path.resolve(__dirname, '../../../')
    return new DockerComposeEnvironment(buildContext, 'docker-compose.yaml')
        .withWaitStrategy('rabbitmq_1', Wait.forHealthCheck())
        .withWaitStrategy('mongo_1', Wait.forHealthCheck())
        .withWaitStrategy('app_1', Wait.forHealthCheck())
        .up();
}

exports.startCompose = startCompose
