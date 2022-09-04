import amqp, { Channel } from 'amqplib'

async function getQueueResponse(queueName: string) {
  const connection = await amqp.connect(globalThis.__BASE_URL__.amqp)
  const channel = await connection.createChannel()
  const message = await channel.get(queueName, { noAck: true })
  await connection.close()
  if (!message) {
    return Promise.reject('No message')
  }
  return JSON.parse(message.content.toString()) as unknown
}

async function getQueueMessageCount(channel: Channel, queueName: string) {
  const queue = await channel.checkQueue(queueName)
  return queue.messageCount
}

async function getMessageCountSum() {
  const connection = await amqp.connect(globalThis.__BASE_URL__.amqp)
  const channel = await connection.createChannel()
  const counts = await Promise.all([
    getQueueMessageCount(channel, 'general.teamData'),
    getQueueMessageCount(channel, 'skill.gate'),
    getQueueMessageCount(channel, 'skill.timer'),
    getQueueMessageCount(channel, 'speed.lap'),
    getQueueMessageCount(channel, 'speed.timer'),
    getQueueMessageCount(channel, 'speed.safetyCar.follow'),
    getQueueMessageCount(channel, 'speed.safetyCar.overtake'),
    getQueueMessageCount(channel, 'team.teamData'),
  ])
  await connection.close()
  return counts.reduce((previousValue, currentValue) => previousValue + currentValue, 0)
}

export { getQueueResponse, getMessageCountSum }
