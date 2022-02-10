import { getMessageCountSum, getQueueResponse } from '../utils/amqp-assertions'
import purgeQueue from '../utils/purge-queue'
import request from 'supertest'

describe('test a likely path of events for skill timer', () => {
  const apiRequest = request(globalThis.__BASE_URL__.app)

  beforeAll(() => purgeQueue('skill.timer'))
  afterAll(() => getMessageCountSum())

  const skillTimerUpdate = { timerAt: 2000, timerAction: 'START' }
  it('should update the skill timer', async () => {
    expect.assertions(3)
    const response = await apiRequest.post('/api/skill/timer').set('RobonAuth-Api-Key', 'BSS').send(skillTimerUpdate)
    expect(response.status).toBe(200)
    expect(response.body).toStrictEqual(skillTimerUpdate)
    await expect(getQueueResponse('skill.timer')).resolves.toStrictEqual(skillTimerUpdate)
  })
})
