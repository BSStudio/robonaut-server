package hu.bsstudio.robonaut

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = [ "hu.bsstudio.robonaut" ])
class DemoApplication

fun main(args: Array<String>) {
    runApplication<DemoApplication>(*args)
}
