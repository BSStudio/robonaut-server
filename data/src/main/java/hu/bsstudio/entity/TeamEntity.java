package hu.bsstudio.entity;

import java.util.List;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@Document
public class TeamEntity {
    @MongoId
    private Long teamId;
    private int year;
    @Indexed
    private String teamName;
    private List<String> teamMembers;
    private TeamType teamType;

    // skill
    private int skillScore;

    // safetyCar
    private int numberOfOvertakes;
    private boolean safetyCarWasFollowed;

    // speed
    private int speedScore;
    private int speedBonusScore;
    private List<Integer> speedTimes;

    // audience
    private int votes;
    private int audienceScore;

    // qualification
    private int qualificationScore;

    // calculated
    private int totalScore;
    private int rank;
    private int juniorRank;
}
