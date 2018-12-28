# Sporoutines

Bringing Kotlin's coroutines to Sponge in a tiny weeny package.

## Example

```kotlin
fun countdown(plugin: Any, player: Player) {
    task(plugin) {
        for (i in 10 downTo 1) {
            player.sendMessage(Text.of("$i!"))
            delay(1, TimeUnit.SECONDS)
        }
        player.sendMessage(Text.of("GO!"))
    }
}
```

## Get It

Sporoutines uses [JitPack](https://jitpack.io) for fetching the artifacts. Current Version: [![Release](https://jitpack.io/v/com.github.ElementsPlugin/Elements.svg)](https://jitpack.io/#ElementsPlugin/Elements)

### Maven:

```xml
<repositories>
    <repositoriy>
        <id>jitpack</id>
        <url>https://jitpack.io</url>
    </repositoriy>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.ElementsPlugin</groupId>
        <artifactId>sporoutines</artifactId>
        <version>CURRENT_VERSION</version>
    </dependency>
</dependencies>
```

### Gradle:

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    compile 'com.github.ElementsPlugin:Sporoutines:CURRENT_VERSION'
}
```