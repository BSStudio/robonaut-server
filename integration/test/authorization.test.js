require('dotenv').config()
const request = require('supertest')

const HOST = process.env.HOST_NAME;

describe('Authorization test', () => {
    const postEndpoint = [
        '/api/team',
        '/api/skill/timer/start',
        '/api/skill/timer/stop',
        '/api/skill/gate',
        '/api/skill/result',
        '/api/speed/safetyCar/follow',
        '/api/speed/safetyCar/overtake',
        '/api/speed/timer/start',
        '/api/speed/timer/stop',
        '/api/speed/lap',
        '/api/speed/result',
        '/api/scores/qualification',
        '/api/scores/audience',
        '/api/scores/endResult'
    ];
    test.each(postEndpoint)('POST %p endpoint should return unauthorized', async (endpoint) => {
        return request(HOST)
            .post(endpoint)
            .then(response => {
                expect(response.status).toBe(401)
            });
    });

    test('GET "/api/team" endpoint should return unauthorized', async () => {
        return request(HOST)
            .post('/api/team')
            .then(response => {
                expect(response.status).toBe(401)
            });
    });

    test('PUT "/api/team" endpoint should return unauthorized', async () => {
        return request(HOST)
            .post('/api/team')
            .then(response => {
                expect(response.status).toBe(401)
            });
    });
});
