require('dotenv').config()
const request = require('supertest')
const amqp = require('amqplib');
const {cleanDB} = require('../src/db-operations')
const {purgeQueue} = require('../src/amqp-operations')

const HOST = process.env.HOST_NAME;
const AMQP_HOST = process.env.AMQP_HOST;

const sentTeam = {
    teamId: 0,
    year: 2021,
    teamName: "BSS",
    teamMembers: ["Boldi", "Bence"],
    teamType: "JUNIOR"
};
const createdTeam = {
    audienceScore: 0,
    juniorRank: 0,
    numberOfOvertakes: 0,
    qualificationScore: 0,
    rank: 0,
    safetyCarWasFollowed: false,
    skillScore: 0,
    speedBonusScore: 0,
    speedScore: 0,
    speedTimes: [],
    teamId: 0,
    teamMembers: [
        "Boldi",
        "Bence"
    ],
    teamName: "BSS",
    teamType: "JUNIOR",
    totalScore: 0,
    votes: 0,
    year: 2021
};
const updateTeam = {
    teamId: 0,
    year: 2022,
    teamName: "Budvári Schönherz Stúdió",
    teamMembers: ["Boldizsár Márta", "Bence Csik"],
    teamType: "SENIOR"
};
const updatedTeam = {
    audienceScore: 0,
    juniorRank: 0,
    numberOfOvertakes: 0,
    qualificationScore: 0,
    rank: 0,
    safetyCarWasFollowed: false,
    skillScore: 0,
    speedBonusScore: 0,
    speedScore: 0,
    speedTimes: [],
    teamId: 0,
    teamMembers: ["Boldizsár Márta", "Bence Csik"],
    teamName: "Budvári Schönherz Stúdió",
    teamType: "SENIOR",
    totalScore: 0,
    votes: 0,
    year: 2022
};
const skillTimerStart = {timerAt: 2000, timerAction: 'START'};
const skillTimerStop = {timerAt: 1000, timerAction: 'STOP'};
const gateInformation = {
    teamId: 0,
    bonusTime: 10,
    timeLeft: 20,
    skillScore: 5,
    totalSkillScore: 25
};
const updatedTeamWithGateInformation = {
    audienceScore: 0,
    juniorRank: 0,
    numberOfOvertakes: 0,
    qualificationScore: 0,
    rank: 0,
    safetyCarWasFollowed: false,
    skillScore: 25,
    speedBonusScore: 0,
    speedScore: 0,
    speedTimes: [],
    teamId: 0,
    teamMembers: ["Boldizsár Márta", "Bence Csik"],
    teamName: "Budvári Schönherz Stúdió",
    teamType: "SENIOR",
    totalScore: 0,
    votes: 0,
    year: 2022
};
const skillResult = {
    teamId: 0,
    skillScore: 50
};
const updatedTeamAfterSkillRace = {
    audienceScore: 0,
    juniorRank: 0,
    numberOfOvertakes: 0,
    qualificationScore: 0,
    rank: 0,
    safetyCarWasFollowed: false,
    skillScore: 50,
    speedBonusScore: 0,
    speedScore: 0,
    speedTimes: [],
    teamId: 0,
    teamMembers: ["Boldizsár Márta", "Bence Csik"],
    teamName: "Budvári Schönherz Stúdió",
    teamType: "SENIOR",
    totalScore: 0,
    votes: 0,
    year: 2022
};
const safetyCarFollowInformation = {
    teamId: 0,
    safetyCarFollowed: true
};
const updatedTeamAfterSafetyCarFollow = {
    audienceScore: 0,
    juniorRank: 0,
    numberOfOvertakes: 0,
    qualificationScore: 0,
    rank: 0,
    safetyCarWasFollowed: true,
    skillScore: 50,
    speedBonusScore: 0,
    speedScore: 0,
    speedTimes: [],
    teamId: 0,
    teamMembers: ["Boldizsár Márta", "Bence Csik"],
    teamName: "Budvári Schönherz Stúdió",
    teamType: "SENIOR",
    totalScore: 0,
    votes: 0,
    year: 2022
};
const safetyCarOvertakeInformation = {
    teamId: 0,
    numberOfOvertakes: 2
};
const updatedTeamAfterSafetyCarOvertake = {
    audienceScore: 0,
    juniorRank: 0,
    numberOfOvertakes: 2,
    qualificationScore: 0,
    rank: 0,
    safetyCarWasFollowed: true,
    skillScore: 50,
    speedBonusScore: 0,
    speedScore: 0,
    speedTimes: [],
    teamId: 0,
    teamMembers: ["Boldizsár Márta", "Bence Csik"],
    teamName: "Budvári Schönherz Stúdió",
    teamType: "SENIOR",
    totalScore: 0,
    votes: 0,
    year: 2022
};
const speedTimerStart = {timerAt: 0, timerAction: 'START'};
const speedTimerStop = {timerAt: 1000, timerAction: 'STOP'};
const speedLapScore = {
    teamId: 0,
    speedTimes: [10, 20, 30]
};
const updatedTeamWithLapInformation = {
    audienceScore: 0,
    juniorRank: 0,
    numberOfOvertakes: 2,
    qualificationScore: 0,
    rank: 0,
    safetyCarWasFollowed: true,
    skillScore: 50,
    speedBonusScore: 0,
    speedScore: 0,
    speedTimes: [10, 20, 30],
    teamId: 0,
    teamMembers: ["Boldizsár Márta", "Bence Csik"],
    teamName: "Budvári Schönherz Stúdió",
    teamType: "SENIOR",
    totalScore: 0,
    votes: 0,
    year: 2022
};
const speedResult = {
    teamId: 0,
    speedScore: 25,
    speedBonusScore: 15,
    speedTimes: [20, 30, 50]
};
const updatedTeamAfterSpeedRace = {
    audienceScore: 0,
    juniorRank: 0,
    numberOfOvertakes: 2,
    qualificationScore: 0,
    rank: 0,
    safetyCarWasFollowed: true,
    skillScore: 50,
    speedBonusScore: 15,
    speedScore: 25,
    speedTimes: [20, 30, 50],
    teamId: 0,
    teamMembers: ["Boldizsár Márta", "Bence Csik"],
    teamName: "Budvári Schönherz Stúdió",
    teamType: "SENIOR",
    totalScore: 0,
    votes: 0,
    year: 2022
};
const qualifiedTeam = {
    teamId: 0,
    qualificationScore: 999
};
const updatedTeamAfterQualification = {
    audienceScore: 0,
    juniorRank: 0,
    numberOfOvertakes: 2,
    qualificationScore: 999,
    rank: 0,
    safetyCarWasFollowed: true,
    skillScore: 50,
    speedBonusScore: 15,
    speedScore: 25,
    speedTimes: [20, 30, 50],
    teamId: 0,
    teamMembers: ["Boldizsár Márta", "Bence Csik"],
    teamName: "Budvári Schönherz Stúdió",
    teamType: "SENIOR",
    totalScore: 0,
    votes: 0,
    year: 2022
};
const audienceScoredTeam = {
    teamId: 0,
    votes: 456,
    audienceScore: 987
}
const updatedTeamAfterAudienceScores = {
    audienceScore: 987,
    juniorRank: 0,
    numberOfOvertakes: 2,
    qualificationScore: 999,
    rank: 0,
    safetyCarWasFollowed: true,
    skillScore: 50,
    speedBonusScore: 15,
    speedScore: 25,
    speedTimes: [20, 30, 50],
    teamId: 0,
    teamMembers: ["Boldizsár Márta", "Bence Csik"],
    teamName: "Budvári Schönherz Stúdió",
    teamType: "SENIOR",
    totalScore: 0,
    votes: 456,
    year: 2022
};
const endResultedTeam = {
    teamId: 0,
    totalScore: 987654,
    rank: 1,
    juniorRank: -1
};
const updatedTeamAfterEndResults = {
    audienceScore: 987,
    juniorRank: -1,
    numberOfOvertakes: 2,
    qualificationScore: 999,
    rank: 1,
    safetyCarWasFollowed: true,
    skillScore: 50,
    speedBonusScore: 15,
    speedScore: 25,
    speedTimes: [20, 30, 50],
    teamId: 0,
    teamMembers: ["Boldizsár Márta", "Bence Csik"],
    teamName: "Budvári Schönherz Stúdió",
    teamType: "SENIOR",
    totalScore: 987654,
    votes: 456,
    year: 2022
};
const adminUpdatedTeam = {
    audienceScore: 9871,
    juniorRank: -11,
    numberOfOvertakes: 21,
    qualificationScore: 9991,
    rank: 11,
    safetyCarWasFollowed: false,
    skillScore: 501,
    speedBonusScore: 151,
    speedScore: 251,
    speedTimes: [20, 30, 50, 1],
    teamId: 0,
    teamMembers: ["Boldizsár Márta", "Bence Csik", "Csili"],
    teamName: "BSS",
    teamType: "JUNIOR",
    totalScore: 9876541,
    votes: 4561,
    year: 2023
};

function assertQueue(queueName, expected) {
    let _connection;
    return amqp.connect(AMQP_HOST)
        .then(connection => {
            _connection = connection
            return connection.createChannel();
        })
        .then(channel => channel.get(queueName, {noAck: true}))
        .then(msg => expect(JSON.parse(msg.content.toString())).toStrictEqual(expected))
        .then(() => _connection.close());
}

describe('Test a happy path of events', () => {
    beforeAll(() => {
        return Promise.all([
            cleanDB(),
            purgeQueue('general.teamData'),
            purgeQueue('skill.gate'),
            purgeQueue('skill.timer'),
            purgeQueue('speed.lap'),
            purgeQueue('speed.safetyCar.follow'),
            purgeQueue('speed.safetyCar.overtake'),
            purgeQueue('speed.timer'),
            purgeQueue('team.teamData')
        ]);
    });
    it('should contain no teams in the start', () => {
        return request(HOST)
            .get('/api/team')
            .set('RobonAuth-Api-Key', 'BSS')
            .then(response => {
                expect(response.status).toBe(200)
                expect(response.body).toStrictEqual([])
            });
    });
    it('should add a new team', () => {
        return request(HOST)
            .post('/api/team')
            .set('RobonAuth-Api-Key', 'BSS')
            .send(sentTeam)
            .then(response => {
                expect(response.status).toBe(200)
                expect(response.body).toStrictEqual(createdTeam)
            })
            .then(_ => assertQueue('team.teamData', createdTeam));
    });
    it('should display the new team', () => {
        return request(HOST)
            .get('/api/team')
            .set('RobonAuth-Api-Key', 'BSS')
            .then(response => {
                expect(response.status).toBe(200)
                expect(response.body).toStrictEqual([createdTeam])
            })
            .then(_ => assertQueue('team.teamData', createdTeam));
    });
    it('should update the team', () => {
        return request(HOST)
            .put('/api/team')
            .set('RobonAuth-Api-Key', 'BSS')
            .send(updateTeam)
            .then(response => {
                expect(response.status).toBe(200)
                expect(response.body).toStrictEqual(updatedTeam)
            })
            .then(_ => assertQueue('team.teamData', updatedTeam));
    });
    it('should display the updated team', () => {
        return request(HOST)
            .get('/api/team')
            .set('RobonAuth-Api-Key', 'BSS')
            .then(response => {
                expect(response.status).toBe(200)
                expect(response.body).toStrictEqual([updatedTeam])
            })
            .then(_ => assertQueue('team.teamData', updatedTeam));
    });
    it('should start the skill timer', () => {
        return request(HOST)
            .post('/api/skill/timer/start')
            .set('RobonAuth-Api-Key', 'BSS')
            .send(skillTimerStart)
            .then(response => {
                expect(response.status).toBe(200)
                expect(response.body).toStrictEqual(skillTimerStart)
            })
            .then(_ => assertQueue('skill.timer', skillTimerStart));
    });
    it('should stop the skill timer', () => {
        return request(HOST)
            .post('/api/skill/timer/stop')
            .set('RobonAuth-Api-Key', 'BSS')
            .send(skillTimerStop)
            .then(response => {
                expect(response.status).toBe(200)
                expect(response.body).toStrictEqual(skillTimerStop)
            })
            .then(_ => assertQueue('skill.timer', skillTimerStop));
    });
    it('should update team on gate enter', () => {
        return request(HOST)
            .post('/api/skill/gate')
            .set('RobonAuth-Api-Key', 'BSS')
            .send(gateInformation)
            .then(response => {
                expect(response.status).toBe(200)
                expect(response.body).toStrictEqual(updatedTeamWithGateInformation)
            })
            .then(_ => assertQueue('skill.gate', gateInformation))
            .then(_ => assertQueue('team.teamData', updatedTeamWithGateInformation));
    });
    it('should update team after skill race', () => {
        return request(HOST)
            .post('/api/skill/result')
            .set('RobonAuth-Api-Key', 'BSS')
            .send(skillResult)
            .then(response => {
                expect(response.status).toBe(200)
                expect(response.body).toStrictEqual(updatedTeamAfterSkillRace)
            })
            .then(_ => assertQueue('team.teamData', updatedTeamAfterSkillRace));
    });
    it('should update team after safety car was followed', () => {
        return request(HOST)
            .post('/api/speed/safetyCar/follow')
            .set('RobonAuth-Api-Key', 'BSS')
            .send(safetyCarFollowInformation)
            .then(response => {
                expect(response.status).toBe(200)
                expect(response.body).toStrictEqual(updatedTeamAfterSafetyCarFollow)
            })
            .then(_ => assertQueue('team.teamData', updatedTeamAfterSafetyCarFollow))
            .then(_ => assertQueue('speed.safetyCar.follow', safetyCarFollowInformation));
    });
    it('should update team after safety car was overtaken', () => {
        return request(HOST)
            .post('/api/speed/safetyCar/overtake')
            .set('RobonAuth-Api-Key', 'BSS')
            .send(safetyCarOvertakeInformation)
            .then(response => {
                expect(response.status).toBe(200)
                expect(response.body).toStrictEqual(updatedTeamAfterSafetyCarOvertake)
            })
            .then(_ => assertQueue('team.teamData', updatedTeamAfterSafetyCarOvertake))
            .then(_ => assertQueue('speed.safetyCar.overtake', safetyCarOvertakeInformation));
    });
    it('should start the speed timer', () => {
        return request(HOST)
            .post('/api/speed/timer/start')
            .set('RobonAuth-Api-Key', 'BSS')
            .send(speedTimerStart)
            .then(response => {
                expect(response.status).toBe(200)
                expect(response.body).toStrictEqual(speedTimerStart)
            })
            .then(_ => assertQueue('speed.timer', speedTimerStart))
    });
    it('should stop the speed timer', () => {
        return request(HOST)
            .post('/api/speed/timer/stop')
            .set('RobonAuth-Api-Key', 'BSS')
            .send(speedTimerStop)
            .then(response => {
                expect(response.status).toBe(200)
                expect(response.body).toStrictEqual(speedTimerStop)
            })
            .then(_ => assertQueue('speed.timer', speedTimerStop));
    });
    it('should update team after lap is completed', () => {
        return request(HOST)
            .post('/api/speed/lap')
            .set('RobonAuth-Api-Key', 'BSS')
            .send(speedLapScore)
            .then(response => {
                expect(response.status).toBe(200)
                expect(response.body).toStrictEqual(updatedTeamWithLapInformation)
            })
            .then(_ => assertQueue('speed.lap', speedLapScore))
            .then(_ => assertQueue('team.teamData', updatedTeamWithLapInformation));
    });
    it('should update team after speed race', () => {
        return request(HOST)
            .post('/api/speed/result')
            .set('RobonAuth-Api-Key', 'BSS')
            .send(speedResult)
            .then(response => {
                expect(response.status).toBe(200)
                expect(response.body).toStrictEqual(updatedTeamAfterSpeedRace)
            })
            .then(_ => assertQueue('team.teamData', updatedTeamAfterSpeedRace));
    });
    it('should update qualification scores for the team', () => {
        return request(HOST)
            .post('/api/scores/qualification')
            .set('RobonAuth-Api-Key', 'BSS')
            .send(qualifiedTeam)
            .then(response => {
                expect(response.status).toBe(200)
                expect(response.body).toStrictEqual([updatedTeamAfterQualification])
            })
            .then(_ => assertQueue('team.teamData', updatedTeamAfterQualification));
    });
    it('should update audience scores for the team', () => {
        return request(HOST)
            .post('/api/scores/audience')
            .set('RobonAuth-Api-Key', 'BSS')
            .send(audienceScoredTeam)
            .then(response => {
                expect(response.status).toBe(200)
                expect(response.body).toStrictEqual([updatedTeamAfterAudienceScores])
            })
            .then(_ => assertQueue('team.teamData', updatedTeamAfterAudienceScores));
    });
    it('should update end result scores for the team', () => {
        return request(HOST)
            .post('/api/scores/endResult')
            .set('RobonAuth-Api-Key', 'BSS')
            .send(endResultedTeam)
            .then(response => {
                expect(response.status).toBe(200)
                expect(response.body).toStrictEqual([updatedTeamAfterEndResults])
            })
            .then(_ => assertQueue('team.teamData', updatedTeamAfterEndResults));
    });
    it('should update all field for the team', () => {
        return request(HOST)
            .put('/api/admin/team')
            .set('RobonAuth-Api-Key', 'BSS')
            .send(adminUpdatedTeam)
            .then(response => {
                expect(response.status).toBe(200)
                expect(response.body).toStrictEqual([adminUpdatedTeam])
            })
            .then(_ => assertQueue('team.teamData', adminUpdatedTeam));
    })
    afterAll(() => {
        return cleanDB();
    });
});
