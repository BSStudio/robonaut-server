import amqp from 'amqplib'

export default async (queue: string) => {
  const connection = await amqp.connect(globalThis.__BASE_URL__.amqp)
  const channel = await connection.createChannel()
  const purgeReply = await channel.purgeQueue(queue)
  await connection.close()
  return purgeReply
}
