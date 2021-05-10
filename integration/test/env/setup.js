import { DockerComposeEnvironment, Wait } from 'testcontainers'
import fs from 'fs'
import mkdirp from 'mkdirp'
import path from 'path'
import testEnvironmentTempDir from './testEnvironmentTempDir'

const BUILD_CONTEXT = path.resolve(__dirname, './../../..')
const COMPOSE_FILE = 'docker-compose.yaml'

export default async function () {
  const dockerComposeEnvironment = await new DockerComposeEnvironment(
    BUILD_CONTEXT,
    COMPOSE_FILE
  )
    .withWaitStrategy('rabbitmq_1', Wait.forHealthCheck())
    .withWaitStrategy('mongo_1', Wait.forHealthCheck())
    .withWaitStrategy('app_1', Wait.forHealthCheck())
    .up()

  global.__DOCKER_COMPOSE_ENVIRONMENT__ = dockerComposeEnvironment

  const rabbitMQ = dockerComposeEnvironment.getContainer('rabbitmq_1')
  const mongo = dockerComposeEnvironment.getContainer('mongo_1')
  const app = dockerComposeEnvironment.getContainer('app_1')

  const baseUrl = {
    app: `http://${app.getHost()}:${app.getMappedPort(8080)}`,
    amqp: `amqp://${rabbitMQ.getHost()}:${rabbitMQ.getMappedPort(5672)}`,
    mongo: `mongodb://${mongo.getHost()}:${mongo.getMappedPort(27017)}/`,
  }

  // use the file system to expose the baseUrls for TestEnvironments
  mkdirp.sync(testEnvironmentTempDir)
  fs.writeFileSync(path.join(testEnvironmentTempDir, 'appBaseUrl'), baseUrl.app)
  fs.writeFileSync(
    path.join(testEnvironmentTempDir, 'amqpBaseUrl'),
    baseUrl.amqp
  )
  fs.writeFileSync(
    path.join(testEnvironmentTempDir, 'mongoBaseUrl'),
    baseUrl.mongo
  )
}
