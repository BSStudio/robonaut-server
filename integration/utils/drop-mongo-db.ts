import { MongoClient } from 'mongodb'

export default async (): Promise<boolean> => {
  const mongoClient = new MongoClient(globalThis.__BASE_URL__.mongo)
  const client = await mongoClient.connect()
  const success = await client.db().dropDatabase()
  await client.close()
  return success
}
