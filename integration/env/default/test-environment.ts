import NodeEnvironment from 'jest-environment-node'

export default class TestEnvironment extends NodeEnvironment {
  async setup(): Promise<void> {
    await super.setup()

    this.global.__BASE_URL__ = {
      app: 'http://0.0.0.0:8080',
      amqp: 'amqp://0.0.0.0:5672',
      mongo: 'mongodb://0.0.0.0:27017/',
    }

    return Promise.resolve()
  }
}
