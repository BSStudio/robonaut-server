import { getMessageCountSum, getQueueResponse } from '../utils/amqp-assertions'
import dropMongoDb from '../utils/drop-mongo-db'
import purgeQueue from '../utils/purge-queue'
import request from 'supertest'

describe('test a likely path of events for a junior team', () => {
  const apiRequest = request(globalThis.__BASE_URL__.app)

  beforeAll(() =>
    Promise.all([
      dropMongoDb(),
      purgeQueue('general.teamData'),
      purgeQueue('skill.gate'),
      purgeQueue('speed.lap'),
      purgeQueue('speed.safetyCar.follow'),
      purgeQueue('speed.safetyCar.overtake'),
      purgeQueue('team.teamData'),
    ])
  )
  afterAll(async () => {
    // eslint-disable-next-line jest/no-standalone-expect
    await expect(getMessageCountSum()).resolves.toBe(0)
  })

  it('should contain no teams in the start', async () => {
    expect.assertions(2)
    const response = await apiRequest.get('/api/team').set('RobonAuth-Api-Key', 'BSS')
    expect(response.status).toBe(200)
    expect(response.body).toStrictEqual([])
  })
  const newTeam = {
    teamId: 0,
    year: 2021,
    teamName: 'BSS',
    teamMembers: ['Boldi', 'Bence'],
    teamType: 'SENIOR',
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
    teamType: 'SENIOR',
    votes: 0,
    year: 2021,
  }
  it('should add a new senior team', async () => {
    expect.assertions(3)
    const response = await apiRequest.post('/api/team').set('RobonAuth-Api-Key', 'BSS').send(newTeam)
    expect(response.status).toBe(200)
    expect(response.body).toStrictEqual(createdTeam)
    await expect(getQueueResponse('team.teamData')).resolves.toStrictEqual(createdTeam)
  })
  it('should display the new senior team', async () => {
    expect.assertions(3)
    const response = await apiRequest.get('/api/team').set('RobonAuth-Api-Key', 'BSS')
    expect(response.status).toBe(200)
    expect(response.body).toStrictEqual([createdTeam])
    await expect(getQueueResponse('team.teamData')).resolves.toStrictEqual(createdTeam)
  })
  const updateTeam = {
    teamId: 0,
    year: 2022,
    teamName: 'Budvári Schönherz Stúdió',
    teamMembers: ['Boldizsár Márta', 'Bence Csik'],
    teamType: 'JUNIOR',
  }
  const updatedTeam = {
    ...createdTeam,
    teamMembers: ['Boldizsár Márta', 'Bence Csik'],
    teamName: 'Budvári Schönherz Stúdió',
    teamType: 'JUNIOR',
    year: 2022,
  }
  it('should update the senior team to a junior team', async () => {
    expect.assertions(3)
    const response = await apiRequest.put('/api/team').set('RobonAuth-Api-Key', 'BSS').send(updateTeam)
    expect(response.status).toBe(200)
    expect(response.body).toStrictEqual(updatedTeam)
    await expect(getQueueResponse('team.teamData')).resolves.toStrictEqual(updatedTeam)
  })
  it('should display the updated junior team', async () => {
    expect.assertions(3)
    const response = await apiRequest.get('/api/team').set('RobonAuth-Api-Key', 'BSS')
    expect(response.status).toBe(200)
    expect(response.body).toStrictEqual([updatedTeam])
    await expect(getQueueResponse('team.teamData')).resolves.toStrictEqual(updatedTeam)
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
  it('should update team on gate enter', async () => {
    expect.assertions(4)
    const response = await apiRequest.post('/api/skill/gate').set('RobonAuth-Api-Key', 'BSS').send(gateInformation)
    expect(response.status).toBe(200)
    expect(response.body).toStrictEqual(updatedTeamWithGateInformation)
    await expect(getQueueResponse('skill.gate')).resolves.toStrictEqual(gateInformation)
    await expect(getQueueResponse('team.teamData')).resolves.toStrictEqual(updatedTeamWithGateInformation)
  })
  const skillResult = {
    teamId: 0,
    skillScore: 50,
  }
  const updatedTeamAfterSkillRace = {
    ...updatedTeamWithGateInformation,
    skillScore: 50,
  }
  it('should update team after skill race', async () => {
    expect.assertions(3)
    const response = await apiRequest.post('/api/skill/result').set('RobonAuth-Api-Key', 'BSS').send(skillResult)
    expect(response.status).toBe(200)
    expect(response.body).toStrictEqual(updatedTeamAfterSkillRace)
    await expect(getQueueResponse('team.teamData')).resolves.toStrictEqual(updatedTeamAfterSkillRace)
  })
  const safetyCarFollowInformation = {
    teamId: 0,
    safetyCarFollowed: true,
  }
  const updatedTeamAfterSafetyCarFollow = {
    ...updatedTeamAfterSkillRace,
    safetyCarWasFollowed: true,
  }
  it('should update team after safety car was followed', async () => {
    expect.assertions(4)
    const response = await apiRequest
      .post('/api/speed/safetyCar/follow')
      .set('RobonAuth-Api-Key', 'BSS')
      .send(safetyCarFollowInformation)
    expect(response.status).toBe(200)
    expect(response.body).toStrictEqual(updatedTeamAfterSafetyCarFollow)
    await expect(getQueueResponse('team.teamData')).resolves.toStrictEqual(updatedTeamAfterSafetyCarFollow)
    await expect(getQueueResponse('speed.safetyCar.follow')).resolves.toStrictEqual(safetyCarFollowInformation)
  })
  const safetyCarOvertakeInformation = {
    teamId: 0,
    numberOfOvertakes: 2,
  }
  const updatedTeamAfterSafetyCarOvertake = {
    ...updatedTeamAfterSafetyCarFollow,
    numberOfOvertakes: 2,
  }
  it('should update team after safety car was overtaken', async () => {
    expect.assertions(4)
    const response = await apiRequest
      .post('/api/speed/safetyCar/overtake')
      .set('RobonAuth-Api-Key', 'BSS')
      .send(safetyCarOvertakeInformation)
    expect(response.status).toBe(200)
    expect(response.body).toStrictEqual(updatedTeamAfterSafetyCarOvertake)
    await expect(getQueueResponse('team.teamData')).resolves.toStrictEqual(updatedTeamAfterSafetyCarOvertake)
    await expect(getQueueResponse('speed.safetyCar.overtake')).resolves.toStrictEqual(safetyCarOvertakeInformation)
  })
  const speedLapScore = {
    teamId: 0,
    speedTimes: [10, 20, 30],
  }
  const updatedTeamWithLapInformation = {
    ...updatedTeamAfterSafetyCarOvertake,
    speedTimes: [10, 20, 30],
  }
  it('should update team after lap is completed', async () => {
    expect.assertions(4)
    const response = await apiRequest.post('/api/speed/lap').set('RobonAuth-Api-Key', 'BSS').send(speedLapScore)
    expect(response.status).toBe(200)
    expect(response.body).toStrictEqual(updatedTeamWithLapInformation)
    await expect(getQueueResponse('speed.lap')).resolves.toStrictEqual(speedLapScore)
    await expect(getQueueResponse('team.teamData')).resolves.toStrictEqual(updatedTeamWithLapInformation)
  })
  const speedResult = {
    teamId: 0,
    speedScore: 25,
    speedBonusScore: 15,
    speedTimes: [20, 30, 50],
  }
  const updatedTeamAfterSpeedRaceSenior = {
    ...updatedTeamWithLapInformation,
    combinedScore: {
      ...updatedTeamWithLapInformation.combinedScore,
      speedScore: 25,
    },
    speedTimes: [20, 30, 50],
  }
  it('should update team after speed race', async () => {
    expect.assertions(3)
    const response = await apiRequest.post('/api/speed/result/senior').set('RobonAuth-Api-Key', 'BSS').send(speedResult)
    expect(response.status).toBe(200)
    expect(response.body).toStrictEqual(updatedTeamAfterSpeedRaceSenior)
    await expect(getQueueResponse('team.teamData')).resolves.toStrictEqual(updatedTeamAfterSpeedRaceSenior)
  })
  const updatedTeamAfterSpeedRaceJunior = {
    ...updatedTeamAfterSpeedRaceSenior,
    juniorScore: {
      ...updatedTeamAfterSpeedRaceSenior.juniorScore,
      speedScore: 25,
    },
    speedTimes: [20, 30, 50],
  }
  it('should update junior score for junior team', async () => {
    expect.assertions(3)
    const response = await apiRequest.post('/api/speed/result/junior').set('RobonAuth-Api-Key', 'BSS').send(speedResult)
    expect(response.status).toBe(200)
    expect(response.body).toStrictEqual(updatedTeamAfterSpeedRaceJunior)
    await expect(getQueueResponse('team.teamData')).resolves.toStrictEqual(updatedTeamAfterSpeedRaceJunior)
  })
  const qualifiedTeam = {
    teamId: 0,
    qualificationScore: 999,
  }
  const updatedTeamAfterQualification = {
    ...updatedTeamAfterSpeedRaceJunior,
    qualificationScore: 999,
  }
  it('should update qualification scores for the team', async () => {
    expect.assertions(3)
    const response = await apiRequest
      .post('/api/scores/qualification')
      .set('RobonAuth-Api-Key', 'BSS')
      .send(qualifiedTeam)
    expect(response.status).toBe(200)
    expect(response.body).toStrictEqual([updatedTeamAfterQualification])
    await expect(getQueueResponse('team.teamData')).resolves.toStrictEqual(updatedTeamAfterQualification)
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
  it('should update audience scores for the team', async () => {
    expect.assertions(3)
    const response = await apiRequest
      .post('/api/scores/audience')
      .set('RobonAuth-Api-Key', 'BSS')
      .send(audienceScoredTeam)
    expect(response.status).toBe(200)
    expect(response.body).toStrictEqual([updatedTeamAfterAudienceScores])
    await expect(getQueueResponse('team.teamData')).resolves.toStrictEqual(updatedTeamAfterAudienceScores)
  })
  const endResultedTeam = {
    teamId: 0,
    totalScore: 987654,
    rank: 1,
    juniorRank: -1,
  }
  const updatedTeamAfterEndResultsSenior = {
    ...updatedTeamAfterAudienceScores,
    combinedScore: {
      ...updatedTeamAfterAudienceScores.combinedScore,
      totalScore: 987654,
    },
  }
  it('should update combined end result scores for junior team', async () => {
    expect.assertions(3)
    const response = await apiRequest
      .post('/api/scores/endResult/senior')
      .set('RobonAuth-Api-Key', 'BSS')
      .send(endResultedTeam)
    expect(response.status).toBe(200)
    expect(response.body).toStrictEqual([updatedTeamAfterEndResultsSenior])
    await expect(getQueueResponse('team.teamData')).resolves.toStrictEqual(updatedTeamAfterEndResultsSenior)
  })
  const updatedTeamAfterEndResultsJunior = {
    ...updatedTeamAfterEndResultsSenior,
    juniorScore: {
      ...updatedTeamAfterEndResultsSenior.juniorScore,
      totalScore: 987654,
    },
  }
  it('should update junior end result scores for junior team', async () => {
    expect.assertions(3)
    const response = await apiRequest
      .post('/api/scores/endResult/junior')
      .set('RobonAuth-Api-Key', 'BSS')
      .send(endResultedTeam)
    expect(response.status).toBe(200)
    expect(response.body).toStrictEqual([updatedTeamAfterEndResultsJunior])
    await expect(getQueueResponse('team.teamData')).resolves.toStrictEqual(updatedTeamAfterEndResultsJunior)
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
  it('should update all field for the team', async () => {
    expect.assertions(3)
    const response = await apiRequest.put('/api/admin/team').set('RobonAuth-Api-Key', 'BSS').send(adminUpdatedTeam)
    expect(response.status).toBe(200)
    expect(response.body).toStrictEqual([adminUpdatedTeam])
    await expect(getQueueResponse('team.teamData')).resolves.toStrictEqual(adminUpdatedTeam)
  })
})
