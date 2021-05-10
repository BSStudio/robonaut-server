import amqp from 'amqplib'

export default (amqpBaseUrl, queue) => {
  let _connection
  return amqp
    .connect(amqpBaseUrl)
    .then((connection) => {
      _connection = connection
      return connection.createChannel()
    })
    .then((channel) => channel.purgeQueue(queue))
    .finally(() => _connection.close())
}
