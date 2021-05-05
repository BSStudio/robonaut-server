const path = require('path')
const { DockerComposeEnvironment, Wait } = require('testcontainers')

describe('Test testcontainers', () => {
    let dockerComposeEnvironment
    let rabbitMQContainer
    let mongoContainer
    let appContainer
    beforeAll(async () => {
        const buildContext = path.resolve(__dirname, '../../')

        dockerComposeEnvironment = await new DockerComposeEnvironment(buildContext, 'docker-compose.yaml')
            .withWaitStrategy('rabbitmq_1', Wait.forHealthCheck())
            .withWaitStrategy('mongo_1', Wait.forHealthCheck())
            .withWaitStrategy('app_1', Wait.forHealthCheck())
            .up();
        rabbitMQContainer = dockerComposeEnvironment.getContainer('rabbitmq_1');
        mongoContainer = dockerComposeEnvironment.getContainer('mongo_1');
        appContainer = dockerComposeEnvironment.getContainer('app_1');
    }, 1000 * 60 * 30)
    it('should run', function () {
        expect(true).toBeTruthy()
    });
    afterAll(async () => {
        await dockerComposeEnvironment.down()
    })
});