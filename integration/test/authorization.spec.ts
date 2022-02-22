import request from 'supertest'

describe('authorization test', () => {
  const apiRequest = request(globalThis.__BASE_URL__.app)

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
  it.each(postEndpoint)('endpoint POST %p should return unauthorized', async (endpoint) => {
    expect.assertions(1)
    const response = await apiRequest.post(endpoint)
    expect(response.status).toBe(401)
  })

  it('endpoint GET "/api/team" should return unauthorized', async () => {
    expect.assertions(1)
    const response = await apiRequest.get('/api/team')
    expect(response.status).toBe(401)
  })

  const putEndpoints = ['/api/admin/team', '/api/team']
  it.each(putEndpoints)('endpoint PUT %p should return unauthorized', async (endpoint) => {
    expect.assertions(1)
    const response = await apiRequest.put(endpoint)
    expect(response.status).toBe(401)
  })
})
