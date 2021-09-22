import * as fs from 'fs/promises'
import NodeEnvironment = require('jest-environment-node')
import * as path from 'path'
import jestTempDir from './jestTempDir'

export default class TestEnvironment extends NodeEnvironment {
  private static readTempFile(name: string) {
    return fs.readFile(path.join(jestTempDir, name), 'utf8')
  }

  async setup(): Promise<void> {
    await super.setup()

    // get the urls
    const app = await TestEnvironment.readTempFile('appBaseUrl')
    const amqp = await TestEnvironment.readTempFile('amqpBaseUrl')
    const mongo = await TestEnvironment.readTempFile('mongoBaseUrl')

    if (!app || !amqp || !mongo) {
      throw new Error('baseUrls not found')
    }

    this.global.__BASE_URL__ = {
      app,
      amqp,
      mongo,
    }

    return Promise.resolve()
  }
}
