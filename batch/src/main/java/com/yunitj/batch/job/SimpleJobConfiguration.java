package com.yunitj.batch.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j // log 사용을 위한 lombok 어노테이션
@RequiredArgsConstructor // 생성자 DI를 위한 lombok 어노테이션
@Configuration
public class SimpleJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory; // 생성자 DI 받음
    private final StepBuilderFactory stepBuilderFactory; // 생성자 DI 받음

    @Bean
    public Job simpleJob() {
        return jobBuilderFactory.get("simpleJob") //simpleJob 이란 이름의 Batch Job을 생성합니다.
                .start(simpleStep1())
                .next(simpleStep2(null))
                .build();
    }

    private final SimpleJobTasklet jobTasklet;

    //JobScope, StepScope 역시 Job이 실행되고 끝날때, Step이 실행되고 끝날때 생성/삭제가 이루어진다고 보시면 됩니다.
    @Bean
    @JobScope //@JobScope에선 stepExecutionContext는 사용할 수 없고, jobParameters와 jobExecutionContext만 사용할 수 있습니다.
   //@JobScope는 Step 선언문에서 사용 가능하고,
    public Step simpleStep1() {
        log.info(">>>>>>>>>>>>>>>> definition simpleStep1");
        return stepBuilderFactory.get("simpleStep1")
                .tasklet(jobTasklet)
                .build();
    }

    @Bean
    @JobScope
    public Step simpleStep2(@Value("#{jobParameters[requestDate]}") String requestDate) {
        return stepBuilderFactory.get("simpleStep2") //simpleStep1 이란 이름의 Batch Step을 생성합니다.
                //Step 안에서 수행될 기능들을 명시합니다.
                //Tasklet은 Step안에서 단일로 수행될 커스텀한 기능들을 선언할때 사용합니다.
                //Tasklet은 어찌보면 Spring MVC의 @Component, @Bean과 비슷한 역할이라고 보셔도 될 것 같습니다.
                //명확한 역할은 없지만, 개발자가 지정한 커스텀한 기능을 위한 단위로 보시면 됩니다.
                .tasklet((contribution, chunkContext) -> {
                    log.info(">>>>> This is Step2");
                    log.info(">>>>> requestDate = {}", requestDate);
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
}