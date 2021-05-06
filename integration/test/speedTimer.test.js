const request = require('supertest')
const {purgeQueue} = require('../src/amqpOperations')
const {assertQueue, expectQueuesToBeEmpty} = require('./utils/amqpAssertions')

describe('Test a likely path of events for speed timer', () => {

    const appBaseUrl = global.__APP_BASE_URL__
    const amqpBaseUrl = global.__AMQP_BASE_URL__

    beforeAll(() => purgeQueue(amqpBaseUrl, 'speed.timer'));

    const speedTimerUpdate = {timerAt: 2000, timerAction: 'START'};
    it('should update the speed timer', async () => {
        await request(appBaseUrl)
            .post('/api/speed/timer')
            .set('RobonAuth-Api-Key', 'BSS')
            .send(speedTimerUpdate)
            .then(response => {
                expect(response.status).toBe(200)
                expect(response.body).toStrictEqual(speedTimerUpdate)
            })
        await assertQueue(amqpBaseUrl, 'speed.timer', speedTimerUpdate);
    });

    afterAll(() => expectQueuesToBeEmpty(amqpBaseUrl))

});