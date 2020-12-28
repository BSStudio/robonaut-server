require('dotenv').config()
const {MongoClient} = require('mongodb')

const DB_URI = process.env.DB_HOST

const cleanDB = () => {
    const mongoClient = new MongoClient(DB_URI)
    return mongoClient.connect()
        .then(client => {
            return client.db().dropDatabase()
        })
}

const createDummyData = () => {
    return Array.from(new Array(1000), (_, i) => i)
        .map((i) => {
            return {
                _id: i,
                year: 2022,
                teamName: "Budvári Sschönherz Stúdió",
                teamMembers: [
                    "Boldizsár Márta",
                    "Bence Csik"
                ],
                teamType: "SENIOR",
                skillScore: 0,
                numberOfOvertakes: 0,
                safetyCarWasFollowed: false,
                speedScore: 0,
                speedBonusScore: 0,
                speedTimes: [],
                votes: 0,
                audienceScore: 0,
                qualificationScore: 0,
                totalScore: 0,
                rank: 0,
                juniorRank: 0
            }
        })
}

const fill = () => {
    const mongoClient = new MongoClient(DB_URI)
    return mongoClient.connect()
        .then(client => client.db('test'))
        .then(db => db.collection('teamEntity'))
        .then(collection => {
            const dummyData = createDummyData();
            return collection.insertMany(dummyData);
        })
}

exports.cleanDB = cleanDB
exports.fill = fill