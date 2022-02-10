import amqp, { Channel } from 'amqplib'

async function assertQueue(queueName: string, expected: unknown) {
  const connection = await amqp.connect(globalThis.__BASE_URL__.amqp)
  const channel = await connection.createChannel()
  const message = await channel.get(queueName, { noAck: true })
  await connection.close()
  if (!message) {
    return Promise.reject('No message')
  }
  expect(JSON.parse(message.content.toString())).toStrictEqual(expected)
}

async function expectQueueToHaveNoMessages(channel: Channel, queueName: string) {
  const queue = await channel.checkQueue(queueName)
  expect(queue).toHaveProperty('messageCount', 0)
}

async function expectQueuesToBeEmpty() {
  const connection = await amqp.connect(globalThis.__BASE_URL__.amqp)
  const channel = await connection.createChannel()
  await Promise.all([
    expectQueueToHaveNoMessages(channel, 'general.teamData'),
    expectQueueToHaveNoMessages(channel, 'skill.gate'),
    expectQueueToHaveNoMessages(channel, 'skill.timer'),
    expectQueueToHaveNoMessages(channel, 'speed.lap'),
    expectQueueToHaveNoMessages(channel, 'speed.timer'),
    expectQueueToHaveNoMessages(channel, 'speed.safetyCar.follow'),
    expectQueueToHaveNoMessages(channel, 'speed.safetyCar.overtake'),
    expectQueueToHaveNoMessages(channel, 'team.teamData'),
  ])
  await connection.close()
}

export { assertQueue, expectQueuesToBeEmpty }
