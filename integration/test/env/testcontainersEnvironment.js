const NodeEnvironment = require('jest-environment-node')
const fs = require('fs')
const path = require('path')
import testEnvironmentTempDir from './testEnvironmentTempDir'

class TestContainersEnvironment extends NodeEnvironment {
  async setup() {
    await super.setup()

    // get the urls
    const appBaseUrl = fs.readFileSync(
      path.join(testEnvironmentTempDir, 'appBaseUrl'),
      'utf8'
    )
    const amqpBaseUrl = fs.readFileSync(
      path.join(testEnvironmentTempDir, 'amqpBaseUrl'),
      'utf8'
    )
    const mongoBaseUrl = fs.readFileSync(
      path.join(testEnvironmentTempDir, 'mongoBaseUrl'),
      'utf8'
    )

    if (!appBaseUrl || !amqpBaseUrl || !mongoBaseUrl) {
      throw new Error('baseUrls not found')
    }

    this.global.__APP_BASE_URL__ = appBaseUrl
    this.global.__AMQP_BASE_URL__ = amqpBaseUrl
    this.global.__MONGO_BASE_URL__ = mongoBaseUrl
  }
}

module.exports = TestContainersEnvironment
