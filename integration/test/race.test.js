const request = require('supertest')

const HOST = process.env.HOST_NAME;

const sentTeam = {
    teamId: 0,
    year: 2021,
    teamName: "BSS",
    teamMembers: [
        "Boldi",
        "Bence"
    ],
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
    teamName: "Budvári Sschönherz Stúdió",
    teamMembers: [
        "Boldizsár Márta",
        "Bence Csik"
    ],
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
    teamMembers: [
        "Boldizsár Márta",
        "Bence Csik"
    ],
    teamName: "Budvári Sschönherz Stúdió",
    teamType: "SENIOR",
    totalScore: 0,
    votes: 0,
    year: 2022
};

describe('Test series of events', () => {
    it('should contain no teams in the start', async () => {
        return request(HOST)
            .get('/api/team')
            .set('RobonAuth-Api-Key', 'BSS')
            .then(response => {
                expect(response.status).toBe(200)
                expect(response.body).toStrictEqual([])
            });
    });
    it('should add a new team', async () => {
        return request(HOST)
            .post('/api/team')
            .set('RobonAuth-Api-Key', 'BSS')
            .send(sentTeam)
            .then(response => {
                expect(response.status).toBe(200)
                expect(response.body).toStrictEqual(createdTeam)
            });
    });
    it('should display the new team', async () => {
        return request(HOST)
            .get('/api/team')
            .set('RobonAuth-Api-Key', 'BSS')
            .then(response => {
                expect(response.status).toBe(200)
                expect(response.body).toStrictEqual([createdTeam])
            });
    });
    it('should update the team', async () => {
        return request(HOST)
            .put('/api/team')
            .set('RobonAuth-Api-Key', 'BSS')
            .send(updateTeam)
            .then(response => {
                expect(response.status).toBe(200)
                expect(response.body).toStrictEqual(updatedTeam)
            });
    });
    it('should display the updated team', async () => {
        return request(HOST)
            .get('/api/team')
            .set('RobonAuth-Api-Key', 'BSS')
            .then(response => {
                expect(response.status).toBe(200)
                expect(response.body).toStrictEqual([updatedTeam])
            });
    });
});