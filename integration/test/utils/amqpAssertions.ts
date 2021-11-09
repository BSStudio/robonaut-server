import amqp = require('amqplib')

function assertQueue(
  amqpHost: string,
  queueName: string,
  expected: unknown
): Promise<never> {
  let _connection
  return amqp
    .connect(amqpHost)
    .then((connection) => {
      _connection = connection
      return connection.createChannel()
    })
    .then((channel) => channel.get(queueName, { noAck: true }))
    .then((msg) => {
      if (!msg) return Promise.reject('No message')
      expect(JSON.parse(msg.content.toString())).toStrictEqual(expected)
    })
    .finally(() => _connection.close())
}

async function expectQueueToHaveNoMessages(channel, queueName) {
  const queue = await channel.checkQueue(queueName)
  expect(queue).toHaveProperty('messageCount', 0)
}

function expectQueuesToBeEmpty(amqpHost: string) {
  let _connection
  return amqp
    .connect(amqpHost)
    .then((connection) => {
      _connection = connection
      return connection.createChannel()
    })
    .then(async (channel) => {
      await expectQueueToHaveNoMessages(channel, 'general.teamData')
      await expectQueueToHaveNoMessages(channel, 'skill.gate')
      await expectQueueToHaveNoMessages(channel, 'skill.timer')
      await expectQueueToHaveNoMessages(channel, 'speed.lap')
      await expectQueueToHaveNoMessages(channel, 'speed.timer')
      await expectQueueToHaveNoMessages(channel, 'speed.safetyCar.follow')
      await expectQueueToHaveNoMessages(channel, 'speed.safetyCar.overtake')
      await expectQueueToHaveNoMessages(channel, 'team.teamData')
    })
    .finally(() => _connection.close())
}

export { assertQueue, expectQueuesToBeEmpty }
