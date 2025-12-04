import type { TestProject } from 'vitest/node';

declare module 'vitest' {
  export interface ProvidedContext {
    app: string;
    amqp: string;
    mongo: string;
  }
}

export async function setup({ provide }: TestProject) {
  provide(
    'app',
    'http://localhost:8080',
  );
  provide(
    'amqp',
    'amqp://localhost:5672',
  );
  provide(
    'mongo',
    'mongodb://localhost:27017/',
  );
}
