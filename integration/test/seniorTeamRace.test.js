const request = require('supertest')
const amqp = require('amqplib');
const {cleanDB} = require('../src/db-operations')
const {purgeQueue} = require('../src/amqp-operations')

const {HOST_NAME, AMQP_HOST} = process.env;

const newTeam = {
    teamId: 0,
    year: 2021,
    teamName: "BSS",
    teamMembers: ["Boldi", "Bence"],
    teamType: "JUNIOR"
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
    teamType: "JUNIOR",
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
    ...createdTeam,
    teamMembers: ["Boldizsár Márta", "Bence Csik"],
    teamName: "Budvári Schönherz Stúdió",
    teamType: "SENIOR",
    year: 2022
};
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
const skillResult = {
    teamId: 0,
    skillScore: 50
};
const updatedTeamAfterSkillRace = {
    ...updatedTeamWithGateInformation,
    skillScore: 50
};
const safetyCarFollowInformation = {
    teamId: 0,
    safetyCarFollowed: true
};
const updatedTeamAfterSafetyCarFollow = {
    ...updatedTeamAfterSkillRace,
    safetyCarWasFollowed: true
};
const safetyCarOvertakeInformation = {
    teamId: 0,
    numberOfOvertakes: 2
};
const updatedTeamAfterSafetyCarOvertake = {
    ...updatedTeamAfterSafetyCarFollow,
    numberOfOvertakes: 2
};
const speedLapScore = {
    teamId: 0,
    speedTimes: [10, 20, 30]
};
const updatedTeamWithLapInformation = {
    ...updatedTeamAfterSafetyCarOvertake,
    speedTimes: [10, 20, 30],
};
const speedResult = {
    teamId: 0,
    speedScore: 25,
    speedBonusScore: 15,
    speedTimes: [20, 30, 50]
};
const updatedTeamAfterSpeedRace = {
    ...updatedTeamWithLapInformation,
    combinedScore: {
        ...updatedTeamWithLapInformation.combinedScore,
        speedScore: 25
    },
    speedTimes: [20, 30, 50]
};
const qualifiedTeam = {
    teamId: 0,
    qualificationScore: 999
};
const updatedTeamAfterQualification = {
    ...updatedTeamAfterSpeedRace,
    qualificationScore: 999
};
const audienceScoredTeam = {
    teamId: 0,
    votes: 456,
    audienceScore: 987
}
const updatedTeamAfterAudienceScores = {
    ...updatedTeamAfterQualification,
    audienceScore: 987,
    votes: 456
};
const endResultedTeam = {
    teamId: 0,
    totalScore: 987654,
    rank: 1,
    juniorRank: -1
};
const updatedTeamAfterEndResults = {
    ...updatedTeamAfterAudienceScores,
    combinedScore: {
        ...updatedTeamAfterAudienceScores.combinedScore,
        totalScore: 987654
    }
};
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

function expectQueuesToBeEmpty() {
    let _connection;
    return amqp.connect(AMQP_HOST)
        .then(connection => {
            _connection = connection
            return connection.createChannel();
        })
        .then(channel => Promise.all([
            expect(channel.checkQueue('general.teamData')).resolves.toHaveProperty('messageCount', 0),
            expect(channel.checkQueue('skill.gate')).resolves.toHaveProperty('messageCount', 0),
            expect(channel.checkQueue('skill.timer')).resolves.toHaveProperty('messageCount', 0),
            expect(channel.checkQueue('speed.lap')).resolves.toHaveProperty('messageCount', 0),
            expect(channel.checkQueue('speed.timer')).resolves.toHaveProperty('messageCount', 0),
            expect(channel.checkQueue('speed.safetyCar.follow')).resolves.toHaveProperty('messageCount', 0),
            expect(channel.checkQueue('speed.safetyCar.overtake')).resolves.toHaveProperty('messageCount', 0),
            expect(channel.checkQueue('team.teamData')).resolves.toHaveProperty('messageCount', 0),
        ]))
        .then(() => _connection.close());

}

describe('Test a likely path of events for a senior team', () => {
    beforeAll(() => {
        return Promise.all([
            cleanDB(),
            purgeQueue('general.teamData'),
            purgeQueue('skill.gate'),
            purgeQueue('speed.lap'),
            purgeQueue('speed.safetyCar.follow'),
            purgeQueue('speed.safetyCar.overtake'),
            purgeQueue('team.teamData')
        ]);
    });
    it('should contain no teams in the start', () => {
        return request(HOST_NAME)
            .get('/api/team')
            .set('RobonAuth-Api-Key', 'BSS')
            .then(response => {
                expect(response.status).toBe(200)
                expect(response.body).toStrictEqual([])
            });
    });
    it('should add a new team', () => {
        return request(HOST_NAME)
            .post('/api/team')
            .set('RobonAuth-Api-Key', 'BSS')
            .send(newTeam)
            .then(response => {
                expect(response.status).toBe(200)
                expect(response.body).toStrictEqual(createdTeam)
            })
            .then(_ => assertQueue('team.teamData', createdTeam));
    });
    it('should display the new team', () => {
        return request(HOST_NAME)
            .get('/api/team')
            .set('RobonAuth-Api-Key', 'BSS')
            .then(response => {
                expect(response.status).toBe(200)
                expect(response.body).toStrictEqual([createdTeam])
            })
            .then(_ => assertQueue('team.teamData', createdTeam));
    });
    it('should update the team', () => {
        return request(HOST_NAME)
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
        return request(HOST_NAME)
            .get('/api/team')
            .set('RobonAuth-Api-Key', 'BSS')
            .then(response => {
                expect(response.status).toBe(200)
                expect(response.body).toStrictEqual([updatedTeam])
            })
            .then(_ => assertQueue('team.teamData', updatedTeam));
    });
    it('should update team on gate enter', () => {
        return request(HOST_NAME)
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
        return request(HOST_NAME)
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
        return request(HOST_NAME)
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
        return request(HOST_NAME)
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
    it('should update team after lap is completed', () => {
        return request(HOST_NAME)
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
        return request(HOST_NAME)
            .post('/api/speed/result/senior')
            .set('RobonAuth-Api-Key', 'BSS')
            .send(speedResult)
            .then(response => {
                expect(response.status).toBe(200)
                expect(response.body).toStrictEqual(updatedTeamAfterSpeedRace)
            })
            .then(_ => assertQueue('team.teamData', updatedTeamAfterSpeedRace));
    });
    it('should not update junior score for senior team', () => {
        return request(HOST_NAME)
            .post('/api/speed/result/junior')
            .set('RobonAuth-Api-Key', 'BSS')
            .send(speedResult)
            .then(response => {
                expect(response.status).toBe(200)
                expect(response.body).toStrictEqual('')
            })
    });
    it('should update qualification scores for the team', () => {
        return request(HOST_NAME)
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
        return request(HOST_NAME)
            .post('/api/scores/audience')
            .set('RobonAuth-Api-Key', 'BSS')
            .send(audienceScoredTeam)
            .then(response => {
                expect(response.status).toBe(200)
                expect(response.body).toStrictEqual([updatedTeamAfterAudienceScores])
            })
            .then(_ => assertQueue('team.teamData', updatedTeamAfterAudienceScores));
    });
    it('should update combined end result scores for senior team', () => {
        return request(HOST_NAME)
            .post('/api/scores/endResult/senior')
            .set('RobonAuth-Api-Key', 'BSS')
            .send(endResultedTeam)
            .then(response => {
                expect(response.status).toBe(200)
                expect(response.body).toStrictEqual([updatedTeamAfterEndResults])
            })
            .then(_ => assertQueue('team.teamData', updatedTeamAfterEndResults));
    });
    it('should not update junior end result scores for junior team', () => {
        return request(HOST_NAME)
            .post('/api/scores/endResult/junior')
            .set('RobonAuth-Api-Key', 'BSS')
            .send(endResultedTeam)
            .then(response => {
                expect(response.status).toBe(200)
                expect(response.body).toStrictEqual([])
            });
    });
    it('should update all field for the team', () => {
        return request(HOST_NAME)
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
        return Promise.all([
            cleanDB(),
            expectQueuesToBeEmpty()
        ]);
    });
});
