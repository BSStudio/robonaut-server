import * as path from 'node:path';
import {
  DockerComposeEnvironment,
  type StartedDockerComposeEnvironment,
  Wait,
} from 'testcontainers';
import type { GlobalSetupContext } from 'vitest/node';

declare module 'vitest' {
  export interface ProvidedContext {
    app: string;
    amqp: string;
    mongo: string;
  }
}

declare global {
  var compose: StartedDockerComposeEnvironment;
}

const BUILD_CONTEXT = path.resolve(__dirname, './../..');
const COMPOSE_FILES = ['docker-compose.yaml', 'docker-compose.ci.yaml'];

export async function setup({ provide }: GlobalSetupContext) {
  const dockerComposeEnvironment = new DockerComposeEnvironment(
    BUILD_CONTEXT,
    COMPOSE_FILES,
  )
    .withBuild()
    .withWaitStrategy('rabbitmq_1', Wait.forHealthCheck())
    .withWaitStrategy('mongo_1', Wait.forHealthCheck())
    .withWaitStrategy('app_1', Wait.forHealthCheck());
  const compose = await dockerComposeEnvironment.up();

  globalThis.compose = compose;

  const rabbitMQ = compose.getContainer('rabbitmq-1');
  const mongo = compose.getContainer('mongo-1');
  const application = compose.getContainer('app-1');

  provide(
    'app',
    `http://${application.getHost()}:${application.getMappedPort(8080)}`,
  );
  provide(
    'amqp',
    `amqp://${rabbitMQ.getHost()}:${rabbitMQ.getMappedPort(5672)}`,
  );
  provide(
    'mongo',
    `mongodb://${mongo.getHost()}:${mongo.getMappedPort(27017)}/`,
  );
}

export async function teardown() {
  await globalThis.compose.down();
}
