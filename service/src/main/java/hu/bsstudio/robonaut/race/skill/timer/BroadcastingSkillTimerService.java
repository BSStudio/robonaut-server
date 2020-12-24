package hu.bsstudio.robonaut.race.skill.timer;

import hu.bsstudio.robonaut.race.skill.timer.model.SkillTimer;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class BroadcastingSkillTimerService implements SkillTimerService {

    @NonNull
    private final RabbitTemplate template;
    @NonNull
    private final SkillTimerService service;

    @Override
    public Mono<SkillTimer> startTimer(final SkillTimer skillTimer) {
        return service.startTimer(skillTimer)
            .doOnNext(this::sendTimerStarted);
    }

    @Override
    public Mono<SkillTimer> stopTimerAt(final SkillTimer skillTimer) {
        return service.stopTimerAt(skillTimer)
            .doOnNext(this::sendTimerStopped);
    }

    private void sendTimerStarted(final SkillTimer timer) {
        template.convertAndSend("skill.startTimer", timer);
    }

    private void sendTimerStopped(final SkillTimer timer) {
        template.convertAndSend("skill.stopTimer", timer);
    }
}
