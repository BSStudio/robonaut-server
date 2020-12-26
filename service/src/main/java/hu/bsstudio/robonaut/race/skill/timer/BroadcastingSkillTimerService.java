package hu.bsstudio.robonaut.race.skill.timer;

import hu.bsstudio.robonaut.race.skill.timer.model.SkillTimer;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class BroadcastingSkillTimerService implements SkillTimerService {

    public static final String SKILL_TIMER_ROUTING_KEY = "skill.timer";

    @NonNull
    private final RabbitTemplate template;
    @NonNull
    private final SkillTimerService service;

    @Override
    public Mono<SkillTimer> startTimer(final SkillTimer skillTimer) {
        return service.startTimer(skillTimer)
            .doOnNext(this::sendSkillTimerData);
    }

    @Override
    public Mono<SkillTimer> stopTimer(final SkillTimer skillTimer) {
        return service.stopTimer(skillTimer)
            .doOnNext(this::sendSkillTimerData);
    }

    private void sendSkillTimerData(final SkillTimer timer) {
        template.convertAndSend(SKILL_TIMER_ROUTING_KEY, timer);
    }
}
