package com.fastcampus.deliverycatalogbatch.config.jobs

import mu.KotlinLogging
import org.springframework.batch.core.Job
import org.springframework.batch.core.explore.JobExplorer
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.batch.core.repository.JobRepository
import org.springframework.boot.autoconfigure.batch.BatchProperties
import org.springframework.boot.autoconfigure.batch.JobLauncherApplicationRunner
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@EnableConfigurationProperties(BatchProperties::class)
@Configuration
class JobLauncherApplicationRunnerConfig {

    companion object {
        private val logger = KotlinLogging.logger {  }
    }

    @Bean
    fun jobLauncherApplicationRunner(
        jobLauncher: JobLauncher, jobExplorer: JobExplorer, jobRepository: JobRepository,
        properties: BatchProperties, jobs: Collection<Job>
    ): JobLauncherApplicationRunner {
        val runner = JobLauncherApplicationRunner(jobLauncher, jobExplorer, jobRepository)
        val jobName = properties.job.name
        if (!jobs.map { it.name }.contains(jobName)) {
            logger.error { ">>> Job Name [$jobName] 찾을 수 없음" }
            throw IllegalArgumentException(">>> Job Name [$jobName] 찾을 수 없음")
        }
        runner.setJobName(jobName)
        return runner
    }
}