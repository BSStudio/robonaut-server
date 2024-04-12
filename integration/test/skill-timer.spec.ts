import { afterAll, beforeAll, describe, test, expect, inject } from 'vitest'
import request from 'supertest'
import { AmqpUtil } from '../utils/index.js'

describe('test a likely path of events for skill timer', () => {
  const amqpUtil = new AmqpUtil(inject('amqp'))
  const apiRequest = request(inject('app'))

  beforeAll(async () => {
    await amqpUtil.purgeQueue('skill.timer')
  })
  afterAll(async () => {
    await expect(amqpUtil.getMessageCountSum()).resolves.toBe(0)
  })

  const skillTimerUpdate = { timerAt: 2000, timerAction: 'START' }
  test('should update the skill timer', async () => {
    expect.assertions(3)
    const response = await apiRequest
      .post('/api/skill/timer')
      .set('RobonAuth-Api-Key', 'BSS')
      .send(skillTimerUpdate)
    expect(response.status).toBe(200)
    expect(response.body).toStrictEqual(skillTimerUpdate)
    await expect(
      amqpUtil.getQueueResponse('skill.timer'),
    ).resolves.toStrictEqual(skillTimerUpdate)
  })
})
