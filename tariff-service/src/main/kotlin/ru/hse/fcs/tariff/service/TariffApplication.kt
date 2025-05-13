package ru.hse.fcs.tariff.service

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TariffApplication

fun main(args: Array<String>) {
	runApplication<TariffApplication>(*args)
}
