package com.rubp.whattoeat

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform