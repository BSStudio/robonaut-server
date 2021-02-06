const request = require('supertest')
const amqp = require('amqplib')
const {purgeQueue} = require('../src/amqp-operations')

const {HOST_NAME, AMQP_HOST} = process.env;

function assertQueue(queueName, expected) {
    let _connection;
    return amqp.connect(AMQP_HOST)
        .then(connection => {
            _connection = connection
            return connection.createChannel();
        })
        .then(channel => channel.get(queueName, {noAck: true}))
        .then(msg => expect(JSON.parse(msg.content.toString())).toStrictEqual(expected))
        .then(() => _connection.close());
}

function expectQueuesToBeEmpty() {
    let _connection;
    return amqp.connect(AMQP_HOST)
        .then(connection => {
            _connection = connection
            return connection.createChannel();
        })
        .then(channel => Promise.all([
            expect(channel.checkQueue('general.teamData')).resolves.toHaveProperty('messageCount', 0),
            expect(channel.checkQueue('skill.gate')).resolves.toHaveProperty('messageCount', 0),
            expect(channel.checkQueue('skill.timer')).resolves.toHaveProperty('messageCount', 0),
            expect(channel.checkQueue('speed.lap')).resolves.toHaveProperty('messageCount', 0),
            expect(channel.checkQueue('speed.timer')).resolves.toHaveProperty('messageCount', 0),
            expect(channel.checkQueue('speed.safetyCar.follow')).resolves.toHaveProperty('messageCount', 0),
            expect(channel.checkQueue('speed.safetyCar.overtake')).resolves.toHaveProperty('messageCount', 0),
            expect(channel.checkQueue('team.teamData')).resolves.toHaveProperty('messageCount', 0),
        ]))
        .then(() => _connection.close());

}

describe('Test a likely path of events for skill timer', () => {
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
        return expectQueuesToBeEmpty()
    })
});
