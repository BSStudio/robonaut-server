import { DockerComposeEnvironment, Wait } from 'testcontainers'
import * as fs from 'fs'
import { mkdirp } from 'mkdirp'
import * as path from 'path'
import jestTempDir from '../jest-temp-dir'

const BUILD_CONTEXT = path.resolve(__dirname, './../../..')
const COMPOSE_FILES = ['docker-compose.yaml', 'docker-compose.ci.yaml']

export default async function () {
  const dockerComposeEnvironment = await new DockerComposeEnvironment(BUILD_CONTEXT, COMPOSE_FILES)
    .withWaitStrategy('rabbitmq_1', Wait.forHealthCheck())
    .withWaitStrategy('mongo_1', Wait.forHealthCheck())
    .withWaitStrategy('app_1', Wait.forHealthCheck())
    .up()

  globalThis.dockerComposeEnvironment = dockerComposeEnvironment

  const rabbitMQ = dockerComposeEnvironment.getContainer('rabbitmq-1')
  const mongo = dockerComposeEnvironment.getContainer('mongo-1')
  const app = dockerComposeEnvironment.getContainer('app-1')

  const baseUrl = {
    app: `http://${app.getHost()}:${app.getMappedPort(8080)}`,
    amqp: `amqp://${rabbitMQ.getHost()}:${rabbitMQ.getMappedPort(5672)}`,
    mongo: `mongodb://${mongo.getHost()}:${mongo.getMappedPort(27017)}/`,
  }
  // use the file system to expose the baseUrls for TestEnvironments
  await mkdirp(jestTempDir)
  return Promise.all(Object.entries(baseUrl).map(([filename, content]) => writeToTempFile(filename, content)))
}

function writeToTempFile(fileName: string, content: string) {
  const filePath = path.join(jestTempDir, fileName)
  return fs.promises.writeFile(filePath, content)
}
