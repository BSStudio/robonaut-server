require('dotenv').config()
const amqp = require('amqplib')

const AMQP_HOST = process.env.AMQP_HOST;

const purgeQueue = (queue) => {
    let _connection;
    return amqp.connect(AMQP_HOST)
        .then(connection => {
            _connection = connection
            return connection.createChannel();
        })
        .then(channel => channel.purgeQueue(queue))
        .then(() => _connection.close())
}

exports.purgeQueue = purgeQueue