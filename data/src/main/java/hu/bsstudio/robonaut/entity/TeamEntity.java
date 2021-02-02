package hu.bsstudio.robonaut.entity;

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

    // audience
    private int votes;
    private int audienceScore;

    // qualification
    private int qualificationScore;

    private ScoreEntity juniorScore;
    private ScoreEntity score;
}
