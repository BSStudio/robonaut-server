import { afterAll, beforeAll, describe, test, expect, inject } from 'vitest'
import request from 'supertest'
import { AmqpUtil, MongoUtil } from '../utils/index.js'

describe('test a likely path of events for a senior team', () => {
  const amqpUtil = new AmqpUtil(inject('amqp'))
  const mongoUtil = new MongoUtil(inject('mongo'))
  const apiRequest = request(inject('app'))

  beforeAll(async () => {
    await Promise.all([
      mongoUtil.dropDatabase(),
      amqpUtil.purgeQueue('general.teamData'),
      amqpUtil.purgeQueue('skill.gate'),
      amqpUtil.purgeQueue('speed.lap'),
      amqpUtil.purgeQueue('speed.safetyCar.follow'),
      amqpUtil.purgeQueue('speed.safetyCar.overtake'),
      amqpUtil.purgeQueue('team.teamData'),
    ])
    return
  })
  afterAll(async () => {
    await expect(amqpUtil.getMessageCountSum()).resolves.toBe(0)
    return
  })

  test('should contain no teams in the start', async () => {
    expect.assertions(2)
    const response = await apiRequest
      .get('/api/team')
      .set('RobonAuth-Api-Key', 'BSS')
    expect(response.status).toBe(200)
    expect(response.body).toStrictEqual([])
  })

  const newTeam = {
    teamId: 0,
    year: 2021,
    teamName: 'BSS',
    teamMembers: ['Boldi', 'Bence'],
    teamType: 'JUNIOR',
  }
  const createdTeam = {
    audienceScore: 0,
    combinedScore: {
      bestSpeedTime: 0,
      speedScore: 0,
      totalScore: 0,
    },
    juniorScore: {
      bestSpeedTime: 0,
      speedScore: 0,
      totalScore: 0,
    },
    numberOfOvertakes: 0,
    qualificationScore: 0,
    safetyCarWasFollowed: false,
    skillScore: 0,
    speedTimes: [],
    teamId: 0,
    teamMembers: ['Boldi', 'Bence'],
    teamName: 'BSS',
    teamType: 'JUNIOR',
    votes: 0,
    year: 2021,
  }
  test('should add a new team', async () => {
    expect.assertions(3)
    const response = await apiRequest
      .post('/api/team')
      .set('RobonAuth-Api-Key', 'BSS')
      .send(newTeam)
    expect(response.status).toBe(200)
    expect(response.body).toStrictEqual(createdTeam)
    await expect(
      amqpUtil.getQueueResponse('team.teamData'),
    ).resolves.toStrictEqual(createdTeam)
  })

  test('should display the new team', async () => {
    expect.assertions(3)
    const response = await apiRequest
      .get('/api/team')
      .set('RobonAuth-Api-Key', 'BSS')
    expect(response.status).toBe(200)
    expect(response.body).toStrictEqual([createdTeam])
    await expect(
      amqpUtil.getQueueResponse('team.teamData'),
    ).resolves.toStrictEqual(createdTeam)
  })

  const updateTeam = {
    teamId: 0,
    year: 2022,
    teamName: 'Budvári Schönherz Stúdió',
    teamMembers: ['Boldizsár Márta', 'Bence Csik'],
    teamType: 'SENIOR',
  }
  const updatedTeam = {
    ...createdTeam,
    teamMembers: ['Boldizsár Márta', 'Bence Csik'],
    teamName: 'Budvári Schönherz Stúdió',
    teamType: 'SENIOR',
    year: 2022,
  }
  test('should update the team', async () => {
    expect.assertions(3)
    const response = await apiRequest
      .put('/api/team')
      .set('RobonAuth-Api-Key', 'BSS')
      .send(updateTeam)
    expect(response.status).toBe(200)
    expect(response.body).toStrictEqual(updatedTeam)
    await expect(
      amqpUtil.getQueueResponse('team.teamData'),
    ).resolves.toStrictEqual(updatedTeam)
  })

  test('should display the updated team', async () => {
    expect.assertions(3)
    const response = await apiRequest
      .get('/api/team')
      .set('RobonAuth-Api-Key', 'BSS')
    expect(response.status).toBe(200)
    expect(response.body).toStrictEqual([updatedTeam])
    await expect(
      amqpUtil.getQueueResponse('team.teamData'),
    ).resolves.toStrictEqual(updatedTeam)
  })

  const gateInformation = {
    teamId: 0,
    bonusTime: 10,
    timeLeft: 20,
    skillScore: 5,
    totalSkillScore: 25,
  }
  const updatedTeamWithGateInformation = {
    ...updatedTeam,
    skillScore: 25,
  }
  test('should update team on gate enter', async () => {
    expect.assertions(4)
    const response = await apiRequest
      .post('/api/skill/gate')
      .set('RobonAuth-Api-Key', 'BSS')
      .send(gateInformation)
    expect(response.status).toBe(200)
    expect(response.body).toStrictEqual(updatedTeamWithGateInformation)
    await expect(
      amqpUtil.getQueueResponse('skill.gate'),
    ).resolves.toStrictEqual(gateInformation)
    await expect(
      amqpUtil.getQueueResponse('team.teamData'),
    ).resolves.toStrictEqual(updatedTeamWithGateInformation)
  })

  const skillResult = {
    teamId: 0,
    skillScore: 50,
  }
  const updatedTeamAfterSkillRace = {
    ...updatedTeamWithGateInformation,
    skillScore: 50,
  }
  test('should update team after skill race', async () => {
    expect.assertions(3)
    const response = await apiRequest
      .post('/api/skill/result')
      .set('RobonAuth-Api-Key', 'BSS')
      .send(skillResult)
    expect(response.status).toBe(200)
    expect(response.body).toStrictEqual(updatedTeamAfterSkillRace)
    await expect(
      amqpUtil.getQueueResponse('team.teamData'),
    ).resolves.toStrictEqual(updatedTeamAfterSkillRace)
  })

  const safetyCarFollowInformation = {
    teamId: 0,
    safetyCarFollowed: true,
  }
  const updatedTeamAfterSafetyCarFollow = {
    ...updatedTeamAfterSkillRace,
    safetyCarWasFollowed: true,
  }
  test('should update team after safety car was followed', async () => {
    expect.assertions(4)
    const response = await apiRequest
      .post('/api/speed/safetyCar/follow')
      .set('RobonAuth-Api-Key', 'BSS')
      .send(safetyCarFollowInformation)
    expect(response.status).toBe(200)
    expect(response.body).toStrictEqual(updatedTeamAfterSafetyCarFollow)
    await expect(
      amqpUtil.getQueueResponse('team.teamData'),
    ).resolves.toStrictEqual(updatedTeamAfterSafetyCarFollow)
    await expect(
      amqpUtil.getQueueResponse('speed.safetyCar.follow'),
    ).resolves.toStrictEqual(safetyCarFollowInformation)
  })

  const safetyCarOvertakeInformation = {
    teamId: 0,
    numberOfOvertakes: 2,
  }
  const updatedTeamAfterSafetyCarOvertake = {
    ...updatedTeamAfterSafetyCarFollow,
    numberOfOvertakes: 2,
  }
  test('should update team after safety car was overtaken', async () => {
    expect.assertions(4)
    const response = await apiRequest
      .post('/api/speed/safetyCar/overtake')
      .set('RobonAuth-Api-Key', 'BSS')
      .send(safetyCarOvertakeInformation)
    expect(response.status).toBe(200)
    expect(response.body).toStrictEqual(updatedTeamAfterSafetyCarOvertake)
    await expect(
      amqpUtil.getQueueResponse('team.teamData'),
    ).resolves.toStrictEqual(updatedTeamAfterSafetyCarOvertake)
    await expect(
      amqpUtil.getQueueResponse('speed.safetyCar.overtake'),
    ).resolves.toStrictEqual(safetyCarOvertakeInformation)
  })

  const speedLapScore = {
    teamId: 0,
    speedTimes: [10, 20, 30],
  }
  const updatedTeamWithLapInformation = {
    ...updatedTeamAfterSafetyCarOvertake,
    speedTimes: [10, 20, 30],
  }
  test('should update team after lap is completed', async () => {
    expect.assertions(4)
    const response = await apiRequest
      .post('/api/speed/lap')
      .set('RobonAuth-Api-Key', 'BSS')
      .send(speedLapScore)
    expect(response.status).toBe(200)
    expect(response.body).toStrictEqual(updatedTeamWithLapInformation)
    await expect(amqpUtil.getQueueResponse('speed.lap')).resolves.toStrictEqual(
      speedLapScore,
    )
    await expect(
      amqpUtil.getQueueResponse('team.teamData'),
    ).resolves.toStrictEqual(updatedTeamWithLapInformation)
  })

  const speedResult = {
    teamId: 0,
    speedScore: 25,
    speedBonusScore: 15,
    speedTimes: [20, 30, 50],
  }
  const updatedTeamAfterSpeedRace = {
    ...updatedTeamWithLapInformation,
    combinedScore: {
      ...updatedTeamWithLapInformation.combinedScore,
      speedScore: 25,
    },
    speedTimes: [20, 30, 50],
  }
  test('should update team after speed race', async () => {
    expect.assertions(3)
    const response = await apiRequest
      .post('/api/speed/result/senior')
      .set('RobonAuth-Api-Key', 'BSS')
      .send(speedResult)
    expect(response.status).toBe(200)
    expect(response.body).toStrictEqual(updatedTeamAfterSpeedRace)
    await expect(
      amqpUtil.getQueueResponse('team.teamData'),
    ).resolves.toStrictEqual(updatedTeamAfterSpeedRace)
  })

  test('should not update junior score for senior team', async () => {
    expect.assertions(2)
    const response = await apiRequest
      .post('/api/speed/result/junior')
      .set('RobonAuth-Api-Key', 'BSS')
      .send(speedResult)
    expect(response.status).toBe(200)
    expect(response.body).toBe('')
  })

  const qualifiedTeam = {
    teamId: 0,
    qualificationScore: 999,
  }
  const updatedTeamAfterQualification = {
    ...updatedTeamAfterSpeedRace,
    qualificationScore: 999,
  }
  test('should update qualification scores for the team', async () => {
    expect.assertions(3)
    const response = await apiRequest
      .post('/api/scores/qualification')
      .set('RobonAuth-Api-Key', 'BSS')
      .send(qualifiedTeam)
    expect(response.status).toBe(200)
    expect(response.body).toStrictEqual([updatedTeamAfterQualification])
    await expect(
      amqpUtil.getQueueResponse('team.teamData'),
    ).resolves.toStrictEqual(updatedTeamAfterQualification)
  })

  const audienceScoredTeam = {
    teamId: 0,
    votes: 456,
    audienceScore: 987,
  }
  const updatedTeamAfterAudienceScores = {
    ...updatedTeamAfterQualification,
    audienceScore: 987,
    votes: 456,
  }
  test('should update audience scores for the team', async () => {
    expect.assertions(3)
    const response = await apiRequest
      .post('/api/scores/audience')
      .set('RobonAuth-Api-Key', 'BSS')
      .send(audienceScoredTeam)
    expect(response.status).toBe(200)
    expect(response.body).toStrictEqual([updatedTeamAfterAudienceScores])
    await expect(
      amqpUtil.getQueueResponse('team.teamData'),
    ).resolves.toStrictEqual(updatedTeamAfterAudienceScores)
  })

  const endResultedTeam = {
    teamId: 0,
    totalScore: 987654,
    rank: 1,
    juniorRank: -1,
  }
  const updatedTeamAfterEndResults = {
    ...updatedTeamAfterAudienceScores,
    combinedScore: {
      ...updatedTeamAfterAudienceScores.combinedScore,
      totalScore: 987654,
    },
  }
  test('should update combined end result scores for senior team', async () => {
    expect.assertions(3)
    const response = await apiRequest
      .post('/api/scores/endResult/senior')
      .set('RobonAuth-Api-Key', 'BSS')
      .send(endResultedTeam)
    expect(response.status).toBe(200)
    expect(response.body).toStrictEqual([updatedTeamAfterEndResults])
    await expect(
      amqpUtil.getQueueResponse('team.teamData'),
    ).resolves.toStrictEqual(updatedTeamAfterEndResults)
  })

  test('should not update junior end result scores for junior team', async () => {
    expect.assertions(2)
    const response = await apiRequest
      .post('/api/scores/endResult/junior')
      .set('RobonAuth-Api-Key', 'BSS')
      .send(endResultedTeam)
    expect(response.status).toBe(200)
    expect(response.body).toStrictEqual([])
  })

  const adminUpdatedTeam = {
    audienceScore: 9871,
    combinedScore: {
      bestSpeedTime: 123456,
      speedScore: 66,
      totalScore: 1234,
    },
    juniorScore: {
      bestSpeedTime: 123456,
      speedScore: 99,
      totalScore: 123,
    },
    numberOfOvertakes: 21,
    qualificationScore: 9991,
    safetyCarWasFollowed: false,
    skillScore: 501,
    speedTimes: [20, 30, 50, 1],
    teamId: 0,
    teamMembers: ['Boldizsár Márta', 'Bence Csik', 'Csili'],
    teamName: 'BSSes',
    teamType: 'JUNIOR',
    votes: 4561,
    year: 2023,
  }
  test('should update all field for the team', async () => {
    expect.assertions(3)
    const response = await apiRequest
      .put('/api/admin/team')
      .set('RobonAuth-Api-Key', 'BSS')
      .send(adminUpdatedTeam)
    expect(response.status).toBe(200)
    expect(response.body).toStrictEqual([adminUpdatedTeam])
    await expect(
      amqpUtil.getQueueResponse('team.teamData'),
    ).resolves.toStrictEqual(adminUpdatedTeam)
  })
})
