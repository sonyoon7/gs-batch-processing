package com.yunitj.batch.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@StepScope//호출때마다생성.. 싱글톤으로 만들면 jobParameters 'cannot be found 에러 발생
public class SimpleJobTasklet implements Tasklet {

    @Value("#{jobParameters[requestDate]}")
    private String requestDate;

    public SimpleJobTasklet(){
        log.info(">>>>>>>>>>>>>>>>>>> tasklet 생성");
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        log.info(">>>>>>>>>>>>>>>>>>> This is Step1");
        log.info(">>>>>>>>>>>>>requestDate= {}" , requestDate);
        return RepeatStatus.FINISHED;
    }
}
