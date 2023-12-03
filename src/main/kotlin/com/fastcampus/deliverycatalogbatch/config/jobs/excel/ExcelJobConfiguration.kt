package com.fastcampus.deliverycatalogbatch.config.jobs.excel

import com.fastcampus.deliverycatalogbatch.repository.Menu
import com.fastcampus.deliverycatalogbatch.repository.MenuRepository
import mu.KotlinLogging
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager
import java.io.File
import java.io.FileInputStream
import java.math.BigDecimal

@Configuration
class ExcelJobConfiguration(
    private val menuRepository: MenuRepository
) {
    @Value("\${server.role-name")
    private lateinit var roleName: String

    companion object {
        private val logger = KotlinLogging.logger {  }
    }

    @Bean
    fun excelJob(jobRepository: JobRepository, platformTransactionManager: PlatformTransactionManager): Job {
        return JobBuilder("excelJob", jobRepository)
            .incrementer(RunIdIncrementer())
            .start(excelStep(null, jobRepository, platformTransactionManager))
            .build()
    }

    @Bean
    @JobScope
    fun excelStep(
        @Value("#{jobParameters[filePath]}") filePath: String?,
        jobRepository: JobRepository,
        platformTransactionManager: PlatformTransactionManager
    ): Step {
        logger.info { ">>> excelStep jobParameters, filePath = $filePath" }
        return StepBuilder("excelStep", jobRepository)
            .tasklet({ _: StepContribution, _: ChunkContext ->
                logger.info { ">>> excelStep 실행, 엑셀 처리" }

                // 엑셀 파일, 워크북, 시트 얻기
                val file = File(filePath)
                val fileInputStream = FileInputStream(file)
                val workbook = XSSFWorkbook(fileInputStream)
                val sheet = workbook.getSheetAt(0)

                val menus = sheet.filterIndexed { index, _ ->
                    index != 0
                }.mapIndexed { index, row ->
                    val storeId = getLongValueFromCell(row.getCell(0))
                    val menuId = getLongValueFromCell(row.getCell(1))
                    val menuName = row.getCell(2).stringCellValue
                    val menuImageUrl = row.getCell(3).stringCellValue
                    val price = BigDecimal(row.getCell(4).stringCellValue)
                    val description = row.getCell(5).stringCellValue

                    logger.info { ">>> row ($index), col = $storeId, $menuId, $menuName, $menuImageUrl, $price, $description" }
                    val menuOptional = menuId?.let { this.menuRepository.findById(it) }
                    if (menuOptional == null || menuOptional.isEmpty) {
                        Menu(
                            storeId = storeId,
                            menuId = menuId ?: 0L,
                            menuName = menuName,
                            menuMainImageUrl = menuImageUrl,
                            price = price,
                            description = description,
                            createdBy = roleName,
                            updatedBy = roleName,
                        )
                    } else {
                        val menu = menuOptional.get()
                        menu.menuName = menuName
                        menu.price = price
                        menu.description = description
                        menu.menuMainImageUrl = menuImageUrl
                        menu.createdBy = roleName
                        menu.updatedBy = roleName
                        menu
                    }
                }.toList()

                // 엑셀 정보를 메뉴에 저장
                this.menuRepository.saveAll(menus)
                RepeatStatus.FINISHED
            }, platformTransactionManager)
            .build()
    }

    private fun getLongValueFromCell(cell: Cell): Long {
        val trimStringCellValue = cell.stringCellValue.trim()
        if (trimStringCellValue.isEmpty()) {
            return 0L
        }
        return trimStringCellValue.toLong()
    }
}