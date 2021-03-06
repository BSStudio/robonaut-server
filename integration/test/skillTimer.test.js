import { assertQueue, expectQueuesToBeEmpty } from './utils/amqpAssertions'
import purgeQueue from './../src/purgeQueue'
import request from 'supertest'

describe('test a likely path of events for skill timer', () => {
  const appBaseUrl = global.__BASE_URL__.app
  const amqpBaseUrl = global.__BASE_URL__.amqp

  beforeAll(() => purgeQueue(amqpBaseUrl, 'skill.timer'))
  afterAll(() => expectQueuesToBeEmpty(amqpBaseUrl))

  const skillTimerUpdate = { timerAt: 2000, timerAction: 'START' }
  it('should update the skill timer', () =>
    request(appBaseUrl)
      .post('/api/skill/timer')
      .set('RobonAuth-Api-Key', 'BSS')
      .send(skillTimerUpdate)
      .then((response) => {
        expect(response.status).toBe(200)
        expect(response.body).toStrictEqual(skillTimerUpdate)
      })
      .finally(() => assertQueue(amqpBaseUrl, 'skill.timer', skillTimerUpdate)))
})
