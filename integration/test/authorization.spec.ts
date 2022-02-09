import request = require('supertest')

describe('authorization test', () => {
  const appBaseUrl = globalThis.__BASE_URL__.app

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
    '/api/scores/endResult/junior',
  ]
  it.each(postEndpoint)(
    'endpoint POST %p should return unauthorized',
    (endpoint) =>
      request(appBaseUrl)
        .post(endpoint)
        .then((response) => {
          expect(response.status).toBe(401)
        })
  )

  it('endpoint GET "/api/team" should return unauthorized', () =>
    request(appBaseUrl)
      .get('/api/team')
      .then((response) => {
        expect(response.status).toBe(401)
      }))

  const putEndpoints = ['/api/admin/team', '/api/team']
  it.each(putEndpoints)(
    'endpoint PUT %p should return unauthorized',
    (endpoint) =>
      request(appBaseUrl)
        .put(endpoint)
        .then((response) => {
          expect(response.status).toBe(401)
        })
  )
})
