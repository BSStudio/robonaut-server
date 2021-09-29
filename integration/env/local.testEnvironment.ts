import NodeEnvironment = require('jest-environment-node')

export default class TestEnvironment extends NodeEnvironment {
  async setup(): Promise<void> {
    await super.setup()

    this.global.__BASE_URL__ = {
      app: 'http://localhost:8080',
      amqp: 'amqp://localhost:5672',
      mongo: 'mongodb://localhost:27017/',
    }

    return Promise.resolve()
  }
}
