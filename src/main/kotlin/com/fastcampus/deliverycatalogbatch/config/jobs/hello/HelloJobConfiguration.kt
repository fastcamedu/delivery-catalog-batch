package com.fastcampus.deliverycatalogbatch.config.jobs.hello

import mu.KotlinLogging
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager

@Configuration
class HelloJobConfiguration(
) {
    companion object {
        private val logger = KotlinLogging.logger {  }
    }

    @Bean
    fun helloJob(jobRepository: JobRepository, platformTransactionManager: PlatformTransactionManager): Job {
        return JobBuilder("helloJob", jobRepository)
            .start(helloStep(null, jobRepository, platformTransactionManager))
            .build()
    }

    @Bean
    @JobScope
    fun helloStep(
        @Value("#{jobParameters[param]}") param: String?,
        jobRepository: JobRepository,
        platformTransactionManager: PlatformTransactionManager): Step {
        logger.info { ">>> helloStep jobParameters, param = $param" }
        return StepBuilder("helloStep", jobRepository)
            .tasklet({ contribution: StepContribution, chunkContext: ChunkContext ->
                logger.info { ">>> helloStep 실행, tasklet 실행" }
                RepeatStatus.FINISHED
            }, platformTransactionManager)
        .build()
    }
}