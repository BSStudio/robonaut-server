const { startCompose } = require("./utils/dockerComposeStarter");
const request = require('supertest')

describe('Authorization test', () => {

    let dockerComposeEnvironment
    let appBaseUrl

    beforeAll(async () => {
        dockerComposeEnvironment = await startCompose()
        const appContainer = dockerComposeEnvironment.getContainer('app_1');
        appBaseUrl = `http://${appContainer.getHost()}:${appContainer.getMappedPort(8080)}`
    }, 1000 * 60 * 30)

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
        return request(appBaseUrl)
            .post(endpoint)
            .then(response => {
                expect(response.status).toBe(401)
            });
    });

    test('GET "/api/team" endpoint should return unauthorized', () => {
        return request(appBaseUrl)
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
        return request(appBaseUrl)
            .put(endpoint)
            .then(response => {
                expect(response.status).toBe(401)
            });
    });

    afterAll(async () => {
        await dockerComposeEnvironment.down()
    })
});
