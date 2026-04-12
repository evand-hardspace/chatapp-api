package com.evandhardspace.chatapp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class ChatAppApplication

fun main(args: Array<String>) {
	runApplication<ChatAppApplication>(*args)
}