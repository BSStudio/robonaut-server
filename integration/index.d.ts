import { StartedDockerComposeEnvironment } from 'testcontainers'

declare global {
  /* eslint-disable no-var */
  var dockerComposeEnvironment: StartedDockerComposeEnvironment
  var __BASE_URL__: { app: string; mongo: string; amqp: string }
  /* eslint-enable no-var */
}
