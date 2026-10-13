import { createWriteStream } from 'node:fs';
import { mkdir } from 'node:fs/promises';
import * as path from 'node:path';
import {
  DockerComposeEnvironment,
  type StartedDockerComposeEnvironment,
  type StartedTestContainer,
  Wait,
} from 'testcontainers';
import type { TestProject } from 'vitest/node';

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

const BUILD_CONTEXT = path.resolve(import.meta.dirname, './../..');
const COMPOSE_FILES = ['docker-compose.yaml', 'docker-compose.ci.yaml'];

export async function setup({ provide }: TestProject) {
  const dockerComposeEnvironment = new DockerComposeEnvironment(
    BUILD_CONTEXT,
    COMPOSE_FILES,
  )
    .withProfiles('app')
    .withBuild()
    .withWaitStrategy('rabbitmq-1', Wait.forHealthCheck())
    .withWaitStrategy('mongo-1', Wait.forHealthCheck())
    .withWaitStrategy('app-1', Wait.forHealthCheck());
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

  await mkdir('reports/container', { recursive: true });
  await Promise.all([rabbitMQ, mongo, application].map(writeToFile));
}

export async function teardown() {
  await globalThis.compose.down();
}

async function writeToFile(container: StartedTestContainer) {
  const name = container.getName();
  const readable = await container.logs();
  readable.pipe(createWriteStream(`reports/container/${name}.log`));
}
