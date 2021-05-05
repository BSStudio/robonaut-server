const amqp = require('amqplib')

function closeConnection(connection) {
    if (connection) connection.close()
}

function purgeQueue(amqpBaseUrl, queue) {
    let _connection;
    return amqp.connect(amqpBaseUrl)
        .then(connection => {
            _connection = connection
            return connection.createChannel();
        })
        .then(channel => channel.purgeQueue(queue))
        .finally(() => closeConnection(_connection))
}

exports.purgeQueue = purgeQueue
