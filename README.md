# Events
| REST                               | Name                   | Description                        | Broadcast                                                |
|------------------------------------|------------------------|------------------------------------|----------------------------------------------------------|
| POST /api/team                     | addTeam                | Add team                           | All team information                                     |
| GET  /api/team                     | getTeams               | Get team information               | All team information                                     |
| PUT  /api/team                     | updateTeam             | Update team information            | All team information                                     |
| POST /api/skill/timer/start        | technicalTimerStart    | -                                  | TechnicalTimer information                               |
| POST /api/skill/timer/stop         | technicalTimerStop     | -                                  | TechnicalTimer information                               |
| POST /api/skill/gate               | technicalScore         | Update technicalScore (each gate)  | bonusTime, timeLeft, technicalScore, totalTechnicalScore | 
| POST /api/skill/result             | technicalResult        | Update technicalScore (at the end) | Team information                                         |
| POST /api/speed/safetyCar/follow   | safetyCarFollow        | Update safetyCarFollow             | SafetyCarInformation                                     |
| POST /api/speed/safetyCar/overtake | safetyCarOvertake      | Update safetyCarOvertake           | SafetyCarInformation                                     |
| POST /api/speed/timer/start        | speedTimerStart        | -                                  | SpeedTimer information                                   |
| POST /api/speed/timer/stop         | speedTimerStop         | -                                  | SpeedTimer information                                   |
| POST /api/speed/lap                | speedTimeScore         | Update speedResults (each lap)     | LapResult                                                |
| POST /api/speed/result             | speedTimeResult        | Update speedResults (at the end)   | Team information                                         |
| POST /api/scores/qualification     | qualification          | Add qualification scores           | All team information                                     |
| POST /api/scores/audience          | audience               | Add votes from audience            | All team information                                     |
| POST /api/scores/endResult         | finalRankingAndResults | Add final scores                   | All team information                                     |

# Message queues
| type    | queue name        | comment        |
|---------|-------------------|----------------|
| general | teamDataRequest   | from CG client |
| skill   | startTimer        |                |
|         | stopTimer         |                |
|         | gate              |                |
| speed   | safetyCarFollow   |                |
|         | safetyCarOvertake |                |
|         | startTimer        |                |
|         | stopTimer         |                |
|         | lap               |                |
| team    | teamData          |                |
