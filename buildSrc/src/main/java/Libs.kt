object Libs {
  const val AndroidGradlePlugin = "com.android.tools.build:gradle:4.0.1"

  object Kotlin {
    const val Version = "1.4.0"

    const val GradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$Version"

    const val StdLibJdk7 = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$Version"

    const val StdLibJdk8 = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$Version"
  }

  object Kotest {
    const val Version = "4.0.6"

    const val RunnerJunit5 = "io.kotest:kotest-runner-junit5-jvm:$Version"

    const val Assertions = "io.kotest:kotest-assertions-core-jvm:$Version"

    const val AssertionsArrow = "io.kotest:kotest-assertions-arrow:$Version"

    const val Property = "io.kotest:kotest-property-jvm:$Version"
  }

  object AndroidX {
    const val CoreKtx = "androidx.core:core-ktx:1.3.1"

    const val AppCompat = "androidx.appcompat:appcompat:1.2.0"

    object Test {
      const val Version = "1.2.0"

      const val CoreKtx = "androidx.test:core-ktx:$Version"

      const val Orchestrator = "androidx.test:orchestrator:$Version"

      const val Runner = "androidx.test:runner:$Version"
    }

    const val TestExtJUnitKtx = "androidx.test.ext:junit-ktx:1.1.1"
  }

  const val Material = "com.google.android.material:material:1.2.1"

  object Hyperion {
    const val Version = "0.9.27"

    const val Plugin = "com.willowtreeapps.hyperion:hyperion-plugin:$Version"

    const val Core = "com.willowtreeapps.hyperion:hyperion-core:$Version"
  }

  const val AutoService = "com.google.auto.service:auto-service:1.0-rc7"

  const val MavenPublishGradlePlugin = "com.vanniktech:gradle-maven-publish-plugin:0.11.1"

  object Detekt {
    const val Version = "1.9.1"

    const val GradlePluginId = "io.gitlab.arturbosch.detekt"

    const val GradlePlugin = "io.gitlab.arturbosch.detekt:detekt-gradle-plugin:$Version"

    const val Formatting = "io.gitlab.arturbosch.detekt:detekt-formatting:$Version"

    const val Cli = "io.gitlab.arturbosch.detekt:detekt-cli:$Version"
  }

  object GradleVersions {
    const val Version = "0.29.0"

    const val GradlePluginId = "com.github.ben-manes.versions"

    const val GradlePlugin = "com.github.ben-manes:gradle-versions-plugin:$Version"
  }

  const val KotlinPoet = "com.squareup:kotlinpoet:1.6.0"

  const val Arrow = "io.arrow-kt:arrow-core:0.10.5"
}
