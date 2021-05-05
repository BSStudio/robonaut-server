const request = require('supertest')
const { purgeQueue } = require('../src/amqp-operations')

const { HOST_NAME } = process.env;

describe.skip('Test a likely path of events for skill timer', () => {
    beforeAll(() => purgeQueue('skill.timer'));
    const skillTimerUpdate = {timerAt: 2000, timerAction: 'START'};
    it('should update the skill timer', () => {
        return request(HOST_NAME)
            .post('/api/skill/timer')
            .set('RobonAuth-Api-Key', 'BSS')
            .send(skillTimerUpdate)
            .then(response => {
                expect(response.status).toBe(200)
                expect(response.body).toStrictEqual(skillTimerUpdate)
            })
            .then(_ => assertQueue('skill.timer', skillTimerUpdate));
    });
    afterAll(() => {
        return expect(expectQueuesToBeEmpty()).resolves;
    })
});
