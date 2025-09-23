import request from 'supertest';
import { describe, expect, inject, test } from 'vitest';

describe('authorization test', () => {
  const app = inject('app');
  const apiRequest = request(app);

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
  ];
  test.each(postEndpoint)(
    'endpoint POST %s should return unauthorized',
    async (endpoint) => {
      expect.assertions(1);
      const response = await apiRequest.post(endpoint);
      expect(response.status).toBe(401);
    },
  );

  test('endpoint GET "/api/team" should return unauthorized', async () => {
    expect.assertions(1);
    const response = await apiRequest.get('/api/team');
    expect(response.status).toBe(401);
  });

  const putEndpoints = ['/api/admin/team', '/api/team'];
  test.each(putEndpoints)(
    'endpoint PUT %s should return unauthorized',
    async (endpoint) => {
      expect.assertions(1);
      const response = await apiRequest.put(endpoint);
      expect(response.status).toBe(401);
    },
  );
});
