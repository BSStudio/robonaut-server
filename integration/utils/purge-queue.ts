import amqp, { Connection } from 'amqplib'

export default (amqpBaseUrl: string, queue: string) => {
  let _connection: Connection
  return amqp
    .connect(amqpBaseUrl)
    .then((connection) => {
      _connection = connection
      return connection.createChannel()
    })
    .then((channel) => channel.purgeQueue(queue))
    .finally(() => _connection.close())
}
