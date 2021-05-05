const request = require('supertest')
const { purgeQueue } = require('../src/amqp-operations')

const { HOST_NAME } = process.env;

describe.skip('Test a likely path of events for speed timer', () => {
    beforeAll(() => purgeQueue('speed.timer'));
    const speedTimerUpdate = {timerAt: 2000, timerAction: 'START'};
    it('should update the speed timer', () => {
        return request(HOST_NAME)
            .post('/api/speed/timer')
            .set('RobonAuth-Api-Key', 'BSS')
            .send(speedTimerUpdate)
            .then(response => {
                expect(response.status).toBe(200)
                expect(response.body).toStrictEqual(speedTimerUpdate)
            })
            .then(_ => assertQueue('speed.timer', speedTimerUpdate));
    });
    afterAll(()=> {
        return expectQueuesToBeEmpty()
    })
});
