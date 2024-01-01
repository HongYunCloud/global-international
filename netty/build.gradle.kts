dependencies {
    api(project(":"))

    compileOnly("io.netty:netty-all:4.1.104.Final")

    compileOnly("net.kyori:adventure-api:4.16.0-SNAPSHOT")
    compileOnly("net.kyori:adventure-nbt:4.16.0-SNAPSHOT")
    compileOnly("net.kyori:adventure-text-serializer-gson:4.16.0-SNAPSHOT")
}