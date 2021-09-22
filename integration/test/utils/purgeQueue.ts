import amqp = require('amqplib')
import PurgeQueue = amqp.Replies.PurgeQueue

export default (amqpBaseUrl: string, queue: string): Promise<PurgeQueue> => {
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
