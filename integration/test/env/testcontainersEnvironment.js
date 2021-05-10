const fs = require('fs')
const NodeEnvironment = require('jest-environment-node')
const path = require('path')
const testEnvironmentTempDir = require('./testEnvironmentTempDir')

class TestContainersEnvironment extends NodeEnvironment {
  async setup() {
    await super.setup()

    // get the urls
    const app = fs.readFileSync(
      path.join(testEnvironmentTempDir.default, 'appBaseUrl'),
      'utf8'
    )
    const amqp = fs.readFileSync(
      path.join(testEnvironmentTempDir.default, 'amqpBaseUrl'),
      'utf8'
    )
    const mongo = fs.readFileSync(
      path.join(testEnvironmentTempDir.default, 'mongoBaseUrl'),
      'utf8'
    )

    if (!app || !amqp || !mongo) {
      throw new Error('baseUrls not found')
    }

    this.global.__BASE_URL__ = {
      app,
      amqp,
      mongo,
    }
  }
}

module.exports = TestContainersEnvironment
