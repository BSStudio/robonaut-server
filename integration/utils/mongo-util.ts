import { MongoClient } from 'mongodb'

export class MongoUtil {
  constructor(private readonly mongoBaseUrl: string) {}

  public async dropDatabase() {
    const client = await MongoClient.connect(this.mongoBaseUrl)
    const success = await client.db().dropDatabase()
    await client.close()
    return success
  }
}
