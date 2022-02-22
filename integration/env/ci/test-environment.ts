import * as fs from 'fs'
import NodeEnvironment = require('jest-environment-node')
import * as path from 'path'
import jestTempDir from '../jest-temp-dir'

export default class TestEnvironment extends NodeEnvironment {
  private static readTempFile(name: string) {
    return fs.promises.readFile(path.join(jestTempDir, name), 'utf8')
  }

  async setup(): Promise<void> {
    await super.setup()

    // get the urls
    const app = await TestEnvironment.readTempFile('app')
    const amqp = await TestEnvironment.readTempFile('amqp')
    const mongo = await TestEnvironment.readTempFile('mongo')

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
