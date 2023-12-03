package com.fastcampus.deliverycatalogbatch

import org.springframework.boot.SpringApplication.exit
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import kotlin.system.exitProcess

@SpringBootApplication
class DeliveryCatalogBatchApplication

fun main(args: Array<String>) {
	val applicationContext = runApplication<DeliveryCatalogBatchApplication>(*args)
	val exitCode = exit(applicationContext)
	exitProcess(exitCode)
}
