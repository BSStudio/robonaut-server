const {MongoClient} = require('mongodb')

const cleanDB = (mongoUri) => {
    const mongoClient = new MongoClient(mongoUri, {useUnifiedTopology: true})
    return mongoClient.connect()
        .then(client => client.db().dropDatabase())
        .finally(() => mongoClient.close())
}

exports.cleanDB = cleanDB
