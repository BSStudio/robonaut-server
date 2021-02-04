require('dotenv').config()
const request = require('supertest')

const {HOST_NAME} = process.env;

describe('Authorization test', () => {
    const postEndpoint = [
        '/api/team',
        '/api/skill/timer',
        '/api/skill/gate',
        '/api/skill/result',
        '/api/speed/safetyCar/follow',
        '/api/speed/safetyCar/overtake',
        '/api/speed/timer',
        '/api/speed/lap',
        '/api/speed/result/senior',
        '/api/speed/result/junior',
        '/api/scores/qualification',
        '/api/scores/audience',
        '/api/scores/endResult/senior',
        '/api/scores/endResult/junior'
    ];
    test.each(postEndpoint)('POST %p endpoint should return unauthorized', (endpoint) => {
        return request(HOST_NAME)
            .post(endpoint)
            .then(response => {
                expect(response.status).toBe(401)
            });
    });
    test('GET "/api/team" endpoint should return unauthorized', () => {
        return request(HOST_NAME)
            .get('/api/team')
            .then(response => {
                expect(response.status).toBe(401)
            });
    });
    const putEndpoints = [
        '/api/admin/team',
        '/api/team',
    ];
    test.each(putEndpoints)('PUT %p endpoint should return unauthorized', (endpoint) => {
        return request(HOST_NAME)
            .put(endpoint)
            .then(response => {
                expect(response.status).toBe(401)
            });
    });
});
