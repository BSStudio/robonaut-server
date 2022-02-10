import { getMessageCountSum, getQueueResponse } from '../utils/amqp-assertions'
import purgeQueue from '../utils/purge-queue'
import request from 'supertest'

describe('test a likely path of events for speed timer', () => {
  const apiRequest = request(globalThis.__BASE_URL__.app)

  beforeAll(() => purgeQueue('speed.timer'))
  afterAll(() => getMessageCountSum())

  const speedTimerUpdate = { timerAt: 2000, timerAction: 'START' }
  it('should update the speed timer', async () => {
    expect.assertions(3)
    const response = await apiRequest.post('/api/speed/timer').set('RobonAuth-Api-Key', 'BSS').send(speedTimerUpdate)
    expect(response.status).toBe(200)
    expect(response.body).toStrictEqual(speedTimerUpdate)
    await expect(getQueueResponse('speed.timer')).resolves.toStrictEqual(speedTimerUpdate)
  })
})
