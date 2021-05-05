const request = require('supertest')
const { assertQueue } = require('./utils/amqpAssertions')
const { startCompose } = require("./utils/dockerComposeStarter");

describe('Test a likely path of events for a junior team', () => {

    let dockerComposeEnvironment
    let appBaseUrl
    let amqpHost

    beforeAll(async () => {
        dockerComposeEnvironment = await startCompose()
        const appContainer = dockerComposeEnvironment.getContainer('app_1');
        const rabbitMQContainer = dockerComposeEnvironment.getContainer('rabbitmq_1');
        appBaseUrl = `http://${appContainer.getHost()}:${appContainer.getMappedPort(8080)}`
        amqpHost = `amqp://${rabbitMQContainer.getHost()}`
    }, 1000 * 60 * 30)

    it('should contain no teams in the start', () => {
        return request(appBaseUrl)
            .get('/api/team')
            .set('RobonAuth-Api-Key', 'BSS')
            .then(response => {
                expect(response.status).toBe(200)
                expect(response.body).toStrictEqual([])
            });
    });
    const newTeam = {
        teamId: 0,
        year: 2021,
        teamName: "BSS",
        teamMembers: ["Boldi", "Bence"],
        teamType: "SENIOR"
    };
    const createdTeam = {
        audienceScore: 0,
        combinedScore: {
            bestSpeedTime: 0,
            speedScore: 0,
            totalScore: 0
        },
        juniorScore: {
            bestSpeedTime: 0,
            speedScore: 0,
            totalScore: 0
        },
        numberOfOvertakes: 0,
        qualificationScore: 0,
        safetyCarWasFollowed: false,
        skillScore: 0,
        speedTimes: [],
        teamId: 0,
        teamMembers: [
            "Boldi",
            "Bence"
        ],
        teamName: "BSS",
        teamType: "SENIOR",
        votes: 0,
        year: 2021
    };
    it('should add a new senior team', () => {
        return request(appBaseUrl)
            .post('/api/team')
            .set('RobonAuth-Api-Key', 'BSS')
            .send(newTeam)
            .then(response => {
                expect(response.status).toBe(200)
                expect(response.body).toStrictEqual(createdTeam)
            })
            .then(_ => assertQueue(amqpHost, 'team.teamData', createdTeam));
    });
    it('should display the new senior team', () => {
        return request(appBaseUrl)
            .get('/api/team')
            .set('RobonAuth-Api-Key', 'BSS')
            .then(response => {
                expect(response.status).toBe(200)
                expect(response.body).toStrictEqual([createdTeam])
            })
            .then(_ => assertQueue(amqpHost, 'team.teamData', createdTeam));
    });
    const updateTeam = {
        teamId: 0,
        year: 2022,
        teamName: "Budvári Schönherz Stúdió",
        teamMembers: ["Boldizsár Márta", "Bence Csik"],
        teamType: "JUNIOR"
    };
    const updatedTeam = {
        ...createdTeam,
        teamMembers: ["Boldizsár Márta", "Bence Csik"],
        teamName: "Budvári Schönherz Stúdió",
        teamType: "JUNIOR",
        year: 2022
    };
    it('should update the senior team  to a junior team', () => {
        return request(appBaseUrl)
            .put('/api/team')
            .set('RobonAuth-Api-Key', 'BSS')
            .send(updateTeam)
            .then(response => {
                expect(response.status).toBe(200)
                expect(response.body).toStrictEqual(updatedTeam)
            })
            .then(_ => assertQueue(amqpHost, 'team.teamData', updatedTeam));
    });
    it('should display the updated junior team', () => {
        return request(appBaseUrl)
            .get('/api/team')
            .set('RobonAuth-Api-Key', 'BSS')
            .then(response => {
                expect(response.status).toBe(200)
                expect(response.body).toStrictEqual([updatedTeam])
            })
            .then(_ => assertQueue(amqpHost, 'team.teamData', updatedTeam));
    });
    const gateInformation = {
        teamId: 0,
        bonusTime: 10,
        timeLeft: 20,
        skillScore: 5,
        totalSkillScore: 25
    };
    const updatedTeamWithGateInformation = {
        ...updatedTeam,
        skillScore: 25
    };
    it('should update team on gate enter', () => {
        return request(appBaseUrl)
            .post('/api/skill/gate')
            .set('RobonAuth-Api-Key', 'BSS')
            .send(gateInformation)
            .then(response => {
                expect(response.status).toBe(200)
                expect(response.body).toStrictEqual(updatedTeamWithGateInformation)
            })
            .then(_ => assertQueue(amqpHost, 'skill.gate', gateInformation))
            .then(_ => assertQueue(amqpHost, 'team.teamData', updatedTeamWithGateInformation));
    });
    const skillResult = {
        teamId: 0,
        skillScore: 50
    };
    const updatedTeamAfterSkillRace = {
        ...updatedTeamWithGateInformation,
        skillScore: 50
    };
    it('should update team after skill race', () => {
        return request(appBaseUrl)
            .post('/api/skill/result')
            .set('RobonAuth-Api-Key', 'BSS')
            .send(skillResult)
            .then(response => {
                expect(response.status).toBe(200)
                expect(response.body).toStrictEqual(updatedTeamAfterSkillRace)
            })
            .then(_ => assertQueue(amqpHost, 'team.teamData', updatedTeamAfterSkillRace));
    });
    const safetyCarFollowInformation = {
        teamId: 0,
        safetyCarFollowed: true
    };
    const updatedTeamAfterSafetyCarFollow = {
        ...updatedTeamAfterSkillRace,
        safetyCarWasFollowed: true
    };
    it('should update team after safety car was followed', () => {
        return request(appBaseUrl)
            .post('/api/speed/safetyCar/follow')
            .set('RobonAuth-Api-Key', 'BSS')
            .send(safetyCarFollowInformation)
            .then(response => {
                expect(response.status).toBe(200)
                expect(response.body).toStrictEqual(updatedTeamAfterSafetyCarFollow)
            })
            .then(_ => assertQueue(amqpHost, 'team.teamData', updatedTeamAfterSafetyCarFollow))
            .then(_ => assertQueue(amqpHost, 'speed.safetyCar.follow', safetyCarFollowInformation));
    });
    const safetyCarOvertakeInformation = {
        teamId: 0,
        numberOfOvertakes: 2
    };
    const updatedTeamAfterSafetyCarOvertake = {
        ...updatedTeamAfterSafetyCarFollow,
        numberOfOvertakes: 2
    };
    it('should update team after safety car was overtaken', () => {
        return request(appBaseUrl)
            .post('/api/speed/safetyCar/overtake')
            .set('RobonAuth-Api-Key', 'BSS')
            .send(safetyCarOvertakeInformation)
            .then(response => {
                expect(response.status).toBe(200)
                expect(response.body).toStrictEqual(updatedTeamAfterSafetyCarOvertake)
            })
            .then(_ => assertQueue(amqpHost, 'team.teamData', updatedTeamAfterSafetyCarOvertake))
            .then(_ => assertQueue(amqpHost, 'speed.safetyCar.overtake', safetyCarOvertakeInformation));
    });
    const speedLapScore = {
        teamId: 0,
        speedTimes: [10, 20, 30]
    };
    const updatedTeamWithLapInformation = {
        ...updatedTeamAfterSafetyCarOvertake,
        speedTimes: [10, 20, 30],
    };
    it('should update team after lap is completed', () => {
        return request(appBaseUrl)
            .post('/api/speed/lap')
            .set('RobonAuth-Api-Key', 'BSS')
            .send(speedLapScore)
            .then(response => {
                expect(response.status).toBe(200)
                expect(response.body).toStrictEqual(updatedTeamWithLapInformation)
            })
            .then(_ => assertQueue(amqpHost, 'speed.lap', speedLapScore))
            .then(_ => assertQueue(amqpHost, 'team.teamData', updatedTeamWithLapInformation));
    });
    const speedResult = {
        teamId: 0,
        speedScore: 25,
        speedBonusScore: 15,
        speedTimes: [20, 30, 50]
    };
    const updatedTeamAfterSpeedRaceSenior = {
        ...updatedTeamWithLapInformation,
        combinedScore: {
            ...updatedTeamWithLapInformation.combinedScore,
            speedScore: 25
        },
        speedTimes: [20, 30, 50]
    };
    it('should update team after speed race', () => {
        return request(appBaseUrl)
            .post('/api/speed/result/senior')
            .set('RobonAuth-Api-Key', 'BSS')
            .send(speedResult)
            .then(response => {
                expect(response.status).toBe(200)
                expect(response.body).toStrictEqual(updatedTeamAfterSpeedRaceSenior)
            })
            .then(_ => assertQueue(amqpHost, 'team.teamData', updatedTeamAfterSpeedRaceSenior));
    });
    const updatedTeamAfterSpeedRaceJunior = {
        ...updatedTeamAfterSpeedRaceSenior,
        juniorScore: {
            ...updatedTeamAfterSpeedRaceSenior.juniorScore,
            speedScore: 25
        },
        speedTimes: [20, 30, 50]
    };
    it('should update junior score for junior team', () => {
        return request(appBaseUrl)
            .post('/api/speed/result/junior')
            .set('RobonAuth-Api-Key', 'BSS')
            .send(speedResult)
            .then(response => {
                expect(response.status).toBe(200)
                expect(response.body).toStrictEqual(updatedTeamAfterSpeedRaceJunior)
            })
            .then(_ => assertQueue(amqpHost, 'team.teamData', updatedTeamAfterSpeedRaceJunior));
    });
    const qualifiedTeam = {
        teamId: 0,
        qualificationScore: 999
    };
    const updatedTeamAfterQualification = {
        ...updatedTeamAfterSpeedRaceJunior,
        qualificationScore: 999
    };
    it('should update qualification scores for the team', () => {
        return request(appBaseUrl)
            .post('/api/scores/qualification')
            .set('RobonAuth-Api-Key', 'BSS')
            .send(qualifiedTeam)
            .then(response => {
                expect(response.status).toBe(200)
                expect(response.body).toStrictEqual([updatedTeamAfterQualification])
            })
            .then(_ => assertQueue(amqpHost, 'team.teamData', updatedTeamAfterQualification));
    });
    const audienceScoredTeam = {
        teamId: 0,
        votes: 456,
        audienceScore: 987
    };
    const updatedTeamAfterAudienceScores = {
        ...updatedTeamAfterQualification,
        audienceScore: 987,
        votes: 456
    };
    it('should update audience scores for the team', () => {
        return request(appBaseUrl)
            .post('/api/scores/audience')
            .set('RobonAuth-Api-Key', 'BSS')
            .send(audienceScoredTeam)
            .then(response => {
                expect(response.status).toBe(200)
                expect(response.body).toStrictEqual([updatedTeamAfterAudienceScores])
            })
            .then(_ => assertQueue(amqpHost, 'team.teamData', updatedTeamAfterAudienceScores));
    });
    const endResultedTeam = {
        teamId: 0,
        totalScore: 987654,
        rank: 1,
        juniorRank: -1
    };
    const updatedTeamAfterEndResultsSenior = {
        ...updatedTeamAfterAudienceScores,
        combinedScore: {
            ...updatedTeamAfterAudienceScores.combinedScore,
            totalScore: 987654
        }
    };
    it('should update combined end result scores for junior team', () => {
        return request(appBaseUrl)
            .post('/api/scores/endResult/senior')
            .set('RobonAuth-Api-Key', 'BSS')
            .send(endResultedTeam)
            .then(response => {
                expect(response.status).toBe(200)
                expect(response.body).toStrictEqual([updatedTeamAfterEndResultsSenior])
            })
            .then(_ => assertQueue(amqpHost, 'team.teamData', updatedTeamAfterEndResultsSenior));
    });
    const updatedTeamAfterEndResultsJunior = {
        ...updatedTeamAfterEndResultsSenior,
        juniorScore: {
            ...updatedTeamAfterEndResultsSenior.juniorScore,
            totalScore: 987654
        }
    };
    it('should update junior end result scores for junior team', () => {
        return request(appBaseUrl)
            .post('/api/scores/endResult/junior')
            .set('RobonAuth-Api-Key', 'BSS')
            .send(endResultedTeam)
            .then(response => {
                expect(response.status).toBe(200)
                expect(response.body).toStrictEqual([updatedTeamAfterEndResultsJunior])
            })
            .then(_ => assertQueue(amqpHost, 'team.teamData', updatedTeamAfterEndResultsJunior));
    });
    const adminUpdatedTeam = {
        audienceScore: 9871,
        combinedScore: {
            bestSpeedTime: 123456,
            speedScore: 66,
            totalScore: 1234
        },
        juniorScore: {
            bestSpeedTime: 123456,
            speedScore: 99,
            totalScore: 123
        },
        numberOfOvertakes: 21,
        qualificationScore: 9991,
        safetyCarWasFollowed: false,
        skillScore: 501,
        speedTimes: [20, 30, 50, 1],
        teamId: 0,
        teamMembers: ["Boldizsár Márta", "Bence Csik", "Csili"],
        teamName: "BSSes",
        teamType: "JUNIOR",
        votes: 4561,
        year: 2023
    };
    it('should update all field for the team', () => {
        return request(appBaseUrl)
            .put('/api/admin/team')
            .set('RobonAuth-Api-Key', 'BSS')
            .send(adminUpdatedTeam)
            .then(response => {
                expect(response.status).toBe(200)
                expect(response.body).toStrictEqual([adminUpdatedTeam])
            })
            .then(_ => assertQueue(amqpHost, 'team.teamData', adminUpdatedTeam));
    })

    afterAll(async () => {
        await dockerComposeEnvironment.down()
    })
});
