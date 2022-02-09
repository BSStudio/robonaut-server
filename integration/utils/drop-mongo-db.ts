import { MongoClient } from 'mongodb'

export default (mongoUri: string): Promise<boolean> => {
  const mongoClient = new MongoClient(mongoUri)
  return mongoClient
    .connect()
    .then((client) => client.db().dropDatabase())
    .finally(() => mongoClient.close())
}
