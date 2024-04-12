import { afterAll, beforeAll, describe, test, expect, inject } from 'vitest'
import request from 'supertest'
import { AmqpUtil } from '../utils/index.js'

describe('test a likely path of events for speed timer', () => {
  const amqpUtil = new AmqpUtil(inject('amqp'))
  const apiRequest = request(inject('app'))

  beforeAll(async () => {
    await amqpUtil.purgeQueue('speed.timer')
    return
  })
  afterAll(async () => {
    await expect(amqpUtil.getMessageCountSum()).resolves.toBe(0)
    return
  })

  const speedTimerUpdate = { timerAt: 2000, timerAction: 'START' }
  test('should update the speed timer', async () => {
    expect.assertions(3)
    const response = await apiRequest
      .post('/api/speed/timer')
      .set('RobonAuth-Api-Key', 'BSS')
      .send(speedTimerUpdate)
    expect(response.status).toBe(200)
    expect(response.body).toStrictEqual(speedTimerUpdate)
    await expect(
      amqpUtil.getQueueResponse('speed.timer'),
    ).resolves.toStrictEqual(speedTimerUpdate)
  })
})
