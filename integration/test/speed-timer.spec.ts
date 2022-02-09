import { assertQueue, expectQueuesToBeEmpty } from '../utils/amqp-assertions'
import purgeQueue from '../utils/purge-queue'
import request = require('supertest')

describe('test a likely path of events for speed timer', () => {
  const appBaseUrl = globalThis.__BASE_URL__.app as string
  const amqpBaseUrl = globalThis.__BASE_URL__.amqp as string

  beforeAll(() => purgeQueue(amqpBaseUrl, 'speed.timer'))
  afterAll(() => expectQueuesToBeEmpty(amqpBaseUrl))

  const speedTimerUpdate = { timerAt: 2000, timerAction: 'START' }
  it('should update the speed timer', () =>
    request(appBaseUrl)
      .post('/api/speed/timer')
      .set('RobonAuth-Api-Key', 'BSS')
      .send(speedTimerUpdate)
      .then((response) => {
        expect(response.status).toBe(200)
        expect(response.body).toStrictEqual(speedTimerUpdate)
      })
      .finally(() => assertQueue(amqpBaseUrl, 'speed.timer', speedTimerUpdate)))
})
