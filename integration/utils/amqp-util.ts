import amqp, { type Channel } from 'amqplib';

export class AmqpUtil {
  constructor(private readonly amqpBaseUrl: string) {}

  async getQueueResponse(queueName: string) {
    const connection = await amqp.connect(this.amqpBaseUrl);
    const channel = await connection.createChannel();
    const message = await channel.get(queueName, { noAck: true });
    await connection.close();
    if (!message) {
      throw new Error('No message in queue');
    }
    return JSON.parse(message.content.toString()) as unknown;
  }

  async getMessageCountSum() {
    const connection = await amqp.connect(this.amqpBaseUrl);
    const channel = await connection.createChannel();
    const counts = await Promise.all([
      this.getQueueMessageCount(channel, 'general.teamData'),
      this.getQueueMessageCount(channel, 'skill.gate'),
      this.getQueueMessageCount(channel, 'skill.timer'),
      this.getQueueMessageCount(channel, 'speed.lap'),
      this.getQueueMessageCount(channel, 'speed.timer'),
      this.getQueueMessageCount(channel, 'speed.safetyCar.follow'),
      this.getQueueMessageCount(channel, 'speed.safetyCar.overtake'),
      this.getQueueMessageCount(channel, 'team.teamData'),
    ]);
    await connection.close();
    return counts.reduce(
      (previousValue, currentValue) => previousValue + currentValue,
      0,
    );
  }

  private async getQueueMessageCount(channel: Channel, queueName: string) {
    const queue = await channel.checkQueue(queueName);
    return queue.messageCount;
  }

  async purgeQueue(queue: string) {
    const connection = await amqp.connect(this.amqpBaseUrl);
    const channel = await connection.createChannel();
    const purgeReply = await channel.purgeQueue(queue);
    await connection.close();
    return purgeReply;
  }
}
