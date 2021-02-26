require('dotenv').config()
const amqp = require('amqplib')

const AMQP_HOST = process.env.AMQP_HOST;

function closeConnection(connection) {
    if (connection) connection.close()
}

const purgeQueue = (queue) => {
    let _connection;
    return amqp.connect(AMQP_HOST)
        .then(connection => {
            _connection = connection
            return connection.createChannel();
        })
        .then(channel => channel.purgeQueue(queue))
        .finally(() => closeConnection(_connection))
}

exports.purgeQueue = purgeQueue