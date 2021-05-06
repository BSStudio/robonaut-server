const request = require('supertest')
const purgeQueue = require('../src/purgeQueue')
const {assertQueue, expectQueuesToBeEmpty} = require('./utils/amqpAssertions')

describe('Test a likely path of events for skill timer', () => {

    const appBaseUrl = global.__APP_BASE_URL__
    const amqpBaseUrl = global.__AMQP_BASE_URL__

    beforeAll(() => purgeQueue(amqpBaseUrl, 'skill.timer'));

    const skillTimerUpdate = {timerAt: 2000, timerAction: 'START'};
    it('should update the skill timer', async () => {
        await request(appBaseUrl)
            .post('/api/skill/timer')
            .set('RobonAuth-Api-Key', 'BSS')
            .send(skillTimerUpdate)
            .then(response => {
                expect(response.status).toBe(200)
                expect(response.body).toStrictEqual(skillTimerUpdate)
            })
        await assertQueue(amqpBaseUrl, 'skill.timer', skillTimerUpdate)
    });

    afterAll(() => expectQueuesToBeEmpty(amqpBaseUrl))

});
