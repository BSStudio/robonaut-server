package hu.bsstudio.robonaut

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = [ "hu.bsstudio.robonaut" ])
class RobonautServerApplication

fun main(args: Array<String>) {
    runApplication<RobonautServerApplication>(*args)
}
