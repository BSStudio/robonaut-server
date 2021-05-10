import { MongoClient } from 'mongodb'

export default (mongoUri) => {
  const mongoClient = new MongoClient(mongoUri, { useUnifiedTopology: true })
  return mongoClient
    .connect()
    .then((client) => client.db().dropDatabase())
    .finally(() => mongoClient.close())
}
