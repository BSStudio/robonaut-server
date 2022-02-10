import { assertQueue, expectQueuesToBeEmpty } from '../utils/amqp-assertions'
import dropMongoDb from '../utils/drop-mongo-db'
import purgeQueue from '../utils/purge-queue'
import request = require('supertest')

describe('test a likely path of events for a senior team', () => {
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
  afterAll(() => expectQueuesToBeEmpty())

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
  it('should add a new team', async () => {
    expect.assertions(3)
    const response = await apiRequest.post('/api/team').set('RobonAuth-Api-Key', 'BSS').send(newTeam)
    expect(response.status).toBe(200)
    expect(response.body).toStrictEqual(createdTeam)
    await assertQueue('team.teamData', createdTeam)
  })

  it('should display the new team', async () => {
    expect.assertions(3)
    const response = await apiRequest.get('/api/team').set('RobonAuth-Api-Key', 'BSS')
    expect(response.status).toBe(200)
    expect(response.body).toStrictEqual([createdTeam])
    await assertQueue('team.teamData', createdTeam)
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
  it('should update the team', async () => {
    expect.assertions(3)
    const response = await apiRequest.put('/api/team').set('RobonAuth-Api-Key', 'BSS').send(updateTeam)
    expect(response.status).toBe(200)
    expect(response.body).toStrictEqual(updatedTeam)
    await assertQueue('team.teamData', updatedTeam)
  })

  it('should display the updated team', async () => {
    expect.assertions(3)
    const response = await apiRequest.get('/api/team').set('RobonAuth-Api-Key', 'BSS')
    expect(response.status).toBe(200)
    expect(response.body).toStrictEqual([updatedTeam])
    await assertQueue('team.teamData', updatedTeam)
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
    await assertQueue('skill.gate', gateInformation)
    await assertQueue('team.teamData', updatedTeamWithGateInformation)
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
    await assertQueue('team.teamData', updatedTeamAfterSkillRace)
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
    await assertQueue('team.teamData', updatedTeamAfterSafetyCarFollow)
    await assertQueue('speed.safetyCar.follow', safetyCarFollowInformation)
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
    await assertQueue('team.teamData', updatedTeamAfterSafetyCarOvertake)
    await assertQueue('speed.safetyCar.overtake', safetyCarOvertakeInformation)
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
    await assertQueue('speed.lap', speedLapScore)
    await assertQueue('team.teamData', updatedTeamWithLapInformation)
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
  it('should update team after speed race', async () => {
    expect.assertions(3)
    const response = await apiRequest.post('/api/speed/result/senior').set('RobonAuth-Api-Key', 'BSS').send(speedResult)
    expect(response.status).toBe(200)
    expect(response.body).toStrictEqual(updatedTeamAfterSpeedRace)
    await assertQueue('team.teamData', updatedTeamAfterSpeedRace)
  })

  it('should not update junior score for senior team', async () => {
    expect.assertions(2)
    const response = await apiRequest.post('/api/speed/result/junior').set('RobonAuth-Api-Key', 'BSS').send(speedResult)
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
  it('should update qualification scores for the team', async () => {
    expect.assertions(3)
    const response = await apiRequest
      .post('/api/scores/qualification')
      .set('RobonAuth-Api-Key', 'BSS')
      .send(qualifiedTeam)
    expect(response.status).toBe(200)
    expect(response.body).toStrictEqual([updatedTeamAfterQualification])
    await assertQueue('team.teamData', updatedTeamAfterQualification)
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
    await assertQueue('team.teamData', updatedTeamAfterAudienceScores)
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
  it('should update combined end result scores for senior team', async () => {
    expect.assertions(3)
    const response = await apiRequest
      .post('/api/scores/endResult/senior')
      .set('RobonAuth-Api-Key', 'BSS')
      .send(endResultedTeam)
    expect(response.status).toBe(200)
    expect(response.body).toStrictEqual([updatedTeamAfterEndResults])
    await assertQueue('team.teamData', updatedTeamAfterEndResults)
  })

  it('should not update junior end result scores for junior team', async () => {
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
  it('should update all field for the team', async () => {
    expect.assertions(3)
    const response = await apiRequest.put('/api/admin/team').set('RobonAuth-Api-Key', 'BSS').send(adminUpdatedTeam)
    expect(response.status).toBe(200)
    expect(response.body).toStrictEqual([adminUpdatedTeam])
    await assertQueue('team.teamData', adminUpdatedTeam)
  })
})
