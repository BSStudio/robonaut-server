import { DockerComposeEnvironment, Wait } from 'testcontainers'
import * as fs from 'fs/promises'
import mkdirp = require('mkdirp')
import * as path from 'path'
import jestTempDir from './jestTempDir'

const BUILD_CONTEXT = path.resolve(__dirname, './../..')
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

  global.dockerComposeEnvironment = dockerComposeEnvironment

  const rabbitMQ = dockerComposeEnvironment.getContainer('rabbitmq_1')
  const mongo = dockerComposeEnvironment.getContainer('mongo_1')
  const app = dockerComposeEnvironment.getContainer('app_1')

  const baseUrl = {
    app: `http://${app.getHost()}:${app.getMappedPort(8080)}`,
    amqp: `amqp://${rabbitMQ.getHost()}:${rabbitMQ.getMappedPort(5672)}`,
    mongo: `mongodb://${mongo.getHost()}:${mongo.getMappedPort(27017)}/`,
  }

  // use the file system to expose the baseUrls for TestEnvironments
  mkdirp.sync(jestTempDir)
  return Promise.all([
    writeToTempFile('appBaseUrl', baseUrl.app),
    writeToTempFile('amqpBaseUrl', baseUrl.amqp),
    writeToTempFile('mongoBaseUrl', baseUrl.mongo),
  ])
}

function writeToTempFile(fileName: string, content: string) {
  const filePath = path.join(jestTempDir, fileName)
  return fs.writeFile(filePath, content)
}
