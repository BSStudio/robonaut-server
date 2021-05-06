const amqp = require('amqplib')

function assertQueue(amqpHost, queueName, expected) {
    let _connection;
    return amqp.connect(amqpHost)
        .then(connection => {
            _connection = connection
            return connection.createChannel();
        })
        .then(channel => channel.get(queueName, {noAck: true}))
        .then(msg => expect(JSON.parse(msg.content.toString())).toStrictEqual(expected))
        .then(() => _connection.close());
}

function expectQueuesToBeEmpty(amqpHost) {
    let _connection;
    return amqp.connect(amqpHost)
        .then(connection => {
            _connection = connection
            return connection.createChannel();
        })
        .then(channel => Promise.all([
            expect(channel.checkQueue('general.teamData')).resolves.toHaveProperty('messageCount', 0),
            expect(channel.checkQueue('skill.gate')).resolves.toHaveProperty('messageCount', 0),
            expect(channel.checkQueue('skill.timer')).resolves.toHaveProperty('messageCount', 0),
            expect(channel.checkQueue('speed.lap')).resolves.toHaveProperty('messageCount', 0),
            expect(channel.checkQueue('speed.timer')).resolves.toHaveProperty('messageCount', 0),
            expect(channel.checkQueue('speed.safetyCar.follow')).resolves.toHaveProperty('messageCount', 0),
            expect(channel.checkQueue('speed.safetyCar.overtake')).resolves.toHaveProperty('messageCount', 0),
            expect(channel.checkQueue('team.teamData')).resolves.toHaveProperty('messageCount', 0),
        ]))
        .then(() => _connection.close());
}

module.exports = { assertQueue, expectQueuesToBeEmpty }
