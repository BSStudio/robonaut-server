require('dotenv').config()
const amqp = require('amqplib')

const AMQP_HOST = process.env.AMQP_HOST;

const purgeQueue = (queue) => {
    return amqp.connect(AMQP_HOST)
        .then(connection => connection.createChannel())
        .then(channel => channel.purgeQueue(queue))
}

exports.purgeQueue = purgeQueue