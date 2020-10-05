package io.mehow.laboratory.gradle

import arrow.core.Nel
import arrow.core.nel
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.file.shouldExist
import io.kotest.matchers.file.shouldNotExist
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.mehow.laboratory.generator.FeatureValuesCollision
import io.mehow.laboratory.generator.FeaturesCollision
import io.mehow.laboratory.generator.InvalidFeatureName
import io.mehow.laboratory.generator.InvalidFeatureValues
import io.mehow.laboratory.generator.InvalidPackageName
import io.mehow.laboratory.generator.NoFeatureValues
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome.FAILED
import org.gradle.testkit.runner.TaskOutcome.SUCCESS
import java.io.File

class GenerateFeaturesTaskSpec : StringSpec({
  lateinit var gradleRunner: GradleRunner

  beforeTest {
    gradleRunner = GradleRunner.create()
      .withPluginClasspath()
      .withArguments("generateFeatureFlags", "--stacktrace")
  }

  afterTest {
    File("src/test/projects").getOutputDirs().forEach(File::cleanUpDir)
  }

  "generates single feature flag" {
    val fixture = "feature-generate-single".toFixture()

    val result = gradleRunner.withProjectDir(fixture).build()

    result.task(":generateFeatureFlags")!!.outcome shouldBe SUCCESS

    val feature = fixture.featureFile("Feature")
    feature.shouldExist()

    feature.readText() shouldContain """
      |enum class Feature(
      |  override val isFallbackValue: Boolean = false
      |) : io.mehow.laboratory.Feature<Feature> {
      |  First(isFallbackValue = true),
      |
      |  Second;
      |}
    """.trimMargin("|")
  }

  "generates multiple feature flags" {
    val fixture = "feature-generate-multiple".toFixture()

    val result = gradleRunner.withProjectDir(fixture).build()

    result.task(":generateFeatureFlags")!!.outcome shouldBe SUCCESS

    val featureA = fixture.featureFile("FeatureA")
    featureA.shouldExist()

    featureA.readText() shouldContain """
      |enum class FeatureA(
      |  override val isFallbackValue: Boolean = false
      |) : Feature<FeatureA> {
      |  FirstA(isFallbackValue = true),
      |
      |  SecondA;
      |}
    """.trimMargin("|")

    val featureB = fixture.featureFile("FeatureB")
    featureB.shouldExist()

    featureB.readText() shouldContain """
      |enum class FeatureB(
      |  override val isFallbackValue: Boolean = false
      |) : Feature<FeatureB> {
      |  FirstB(isFallbackValue = true),
      |
      |  SecondB;
      |}
    """.trimMargin("|")
  }

  "generates a single feature flag with source" {
    val fixture = "feature-generate-sources-single".toFixture()

    val result = gradleRunner.withProjectDir(fixture).build()

    result.task(":generateFeatureFlags")!!.outcome shouldBe SUCCESS

    val feature = fixture.featureFile("Feature")
    feature.shouldExist()

    feature.readText() shouldContain """
      |enum class Feature(
      |  override val isFallbackValue: Boolean = false
      |) : io.mehow.laboratory.Feature<Feature> {
      |  First(isFallbackValue = true),
      |
      |  Second;
      |
      |  @Suppress("UNCHECKED_CAST")
      |  override val sourcedWith: Class<io.mehow.laboratory.Feature<*>> = Source::class.java as
      |      Class<io.mehow.laboratory.Feature<*>>
      |
      |  enum class Source(
      |    override val isFallbackValue: Boolean = false
      |  ) : io.mehow.laboratory.Feature<Source> {
      |    Local(isFallbackValue = true),
      |
      |    RemoteA,
      |
      |    RemoteB;
      |  }
      |}
    """.trimMargin("|")
  }

  "generates an internal feature flag with source" {
    val fixture = "feature-generate-sources-internal".toFixture()

    val result = gradleRunner.withProjectDir(fixture).build()

    result.task(":generateFeatureFlags")!!.outcome shouldBe SUCCESS

    val feature = fixture.featureFile("Feature")
    feature.shouldExist()

    feature.readText() shouldContain """
      |internal enum class Feature(
      |  override val isFallbackValue: Boolean = false
      |) : io.mehow.laboratory.Feature<Feature> {
      |  First(isFallbackValue = true),
      |
      |  Second;
      |
      |  @Suppress("UNCHECKED_CAST")
      |  override val sourcedWith: Class<io.mehow.laboratory.Feature<*>> = Source::class.java as
      |      Class<io.mehow.laboratory.Feature<*>>
      |
      |  internal enum class Source(
      |    override val isFallbackValue: Boolean = false
      |  ) : io.mehow.laboratory.Feature<Source> {
      |    Local(isFallbackValue = true),
      |
      |    RemoteA,
      |
      |    RemoteB;
      |  }
      |}
    """.trimMargin("|")
  }

  "generates a public feature flag with source" {
    val fixture = "feature-generate-sources-public".toFixture()

    val result = gradleRunner.withProjectDir(fixture).build()

    result.task(":generateFeatureFlags")!!.outcome shouldBe SUCCESS

    val feature = fixture.featureFile("Feature")
    feature.shouldExist()

    feature.readText() shouldContain """
      |
      |enum class Feature(
      |  override val isFallbackValue: Boolean = false
      |) : io.mehow.laboratory.Feature<Feature> {
      |  First(isFallbackValue = true),
      |
      |  Second;
      |
      |  @Suppress("UNCHECKED_CAST")
      |  override val sourcedWith: Class<io.mehow.laboratory.Feature<*>> = Source::class.java as
      |      Class<io.mehow.laboratory.Feature<*>>
      |
      |  enum class Source(
      |    override val isFallbackValue: Boolean = false
      |  ) : io.mehow.laboratory.Feature<Source> {
      |    Local(isFallbackValue = true),
      |
      |    RemoteA,
      |
      |    RemoteB;
      |  }
      |}
    """.trimMargin("|")
  }

  "generates multiple feature flags with sources" {
    val fixture = "feature-generate-sources-multiple".toFixture()

    val result = gradleRunner.withProjectDir(fixture).build()

    result.task(":generateFeatureFlags")!!.outcome shouldBe SUCCESS

    val featureA = fixture.featureFile("FeatureA")
    featureA.shouldExist()

    featureA.readText() shouldContain """
      |enum class FeatureA(
      |  override val isFallbackValue: Boolean = false
      |) : Feature<FeatureA> {
      |  First(isFallbackValue = true),
      |
      |  Second;
      |
      |  @Suppress("UNCHECKED_CAST")
      |  override val sourcedWith: Class<Feature<*>> = Source::class.java as Class<Feature<*>>
      |
      |  enum class Source(
      |    override val isFallbackValue: Boolean = false
      |  ) : Feature<Source> {
      |    Local(isFallbackValue = true),
      |
      |    RemoteA,
      |
      |    RemoteB;
      |  }
      |}
    """.trimMargin("|")

    val featureB = fixture.featureFile("FeatureB")
    featureB.shouldExist()

    featureB.readText() shouldContain """
      |enum class FeatureB(
      |  override val isFallbackValue: Boolean = false
      |) : Feature<FeatureB> {
      |  First(isFallbackValue = true),
      |
      |  Second;
      |}
    """.trimMargin("|")

    val featureC = fixture.featureFile("FeatureC")
    featureC.shouldExist()

    featureC.readText() shouldContain """
      |enum class FeatureC(
      |  override val isFallbackValue: Boolean = false
      |) : Feature<FeatureC> {
      |  First(isFallbackValue = true),
      |
      |  Second;
      |
      |  @Suppress("UNCHECKED_CAST")
      |  override val sourcedWith: Class<Feature<*>> = Source::class.java as Class<Feature<*>>
      |
      |  enum class Source(
      |    override val isFallbackValue: Boolean = false
      |  ) : Feature<Source> {
      |    Local,
      |
      |    RemoteA(isFallbackValue = true),
      |
      |    RemoteC;
      |  }
      |}
    """.trimMargin("|")
  }

  "uses implicit package name" {
    val fixture = "feature-package-name-implicit".toFixture()

    gradleRunner.withProjectDir(fixture).build()

    val feature = fixture.featureFile("io.mehow.implicit.Feature")
    feature.shouldExist()

    feature.readText() shouldContain "package io.mehow.implicit"
  }

  "cascades implicit package name" {
    val fixture = "feature-package-name-implicit-cascading".toFixture()

    gradleRunner.withProjectDir(fixture).build()

    val featureA = fixture.featureFile("io.mehow.implicit.FeatureA")
    featureA.shouldExist()

    val featureB = fixture.featureFile("io.mehow.implicit.FeatureB")
    featureB.shouldExist()
  }

  "switches implicit package name" {
    val fixture = "feature-package-name-implicit-switching".toFixture()

    gradleRunner.withProjectDir(fixture).build()

    val featureA = fixture.featureFile("io.mehow.implicit.FeatureA")
    featureA.shouldExist()

    featureA.readText() shouldContain "package io.mehow.implicit"

    val featureB = fixture.featureFile("io.mehow.implicit.switch.FeatureB")
    featureB.shouldExist()

    featureB.readText() shouldContain "package io.mehow.implicit.switch"
  }

  "uses explicit package name" {
    val fixture = "feature-package-name-explicit".toFixture()

    gradleRunner.withProjectDir(fixture).build()

    val feature = fixture.featureFile("io.mehow.explicit.Feature")
    feature.shouldExist()

    feature.readText() shouldContain "package io.mehow.explicit"
  }

  "switches explicit package name" {
    val fixture = "feature-package-name-explicit-switching".toFixture()

    gradleRunner.withProjectDir(fixture).build()

    val featureA = fixture.featureFile("io.mehow.explicit.Feature")
    featureA.shouldExist()

    featureA.readText() shouldContain "package io.mehow.explicit"

    val featureB = fixture.featureFile("io.mehow.explicit.switch.Feature")
    featureB.shouldExist()

    featureB.readText() shouldContain "package io.mehow.explicit.switch"
  }

  "overrides implicit package name" {
    val fixture = "feature-package-name-explicit-override".toFixture()

    gradleRunner.withProjectDir(fixture).build()

    val feature = fixture.featureFile("io.mehow.explicit.Feature")
    feature.shouldExist()

    feature.readText() shouldContain "package io.mehow.explicit"
  }

  "generates internal feature flag" {
    val fixture = "feature-generate-internal".toFixture()

    gradleRunner.withProjectDir(fixture).build()

    val feature = fixture.featureFile("Feature")
    feature.shouldExist()

    feature.readText() shouldContain "internal enum class Feature"
  }

  "generates public feature flag" {
    val fixture = "feature-generate-public".toFixture()

    gradleRunner.withProjectDir(fixture).build()

    val feature = fixture.featureFile("Feature")
    feature.shouldExist()

    // Ensure public by checking a new line before enum declaration.
    // Change after https://github.com/square/kotlinpoet/pull/933
    feature.readText() shouldContain """
      |
      |enum class Feature
    """.trimMargin("|")
  }

  "generates features with same values but different names" {
    val fixture = "feature-generate-common-values".toFixture()

    gradleRunner.withProjectDir(fixture).build()

    val featureA = fixture.featureFile("FeatureA")
    featureA.shouldExist()

    featureA.readText() shouldContain """
      |enum class FeatureA(
      |  override val isFallbackValue: Boolean = false
      |) : Feature<FeatureA> {
      |  First(isFallbackValue = true),
      |
      |  Second;
      |}
    """.trimMargin("|")

    val featureB = fixture.featureFile("FeatureB")
    featureB.shouldExist()

    featureB.readText() shouldContain """
      |enum class FeatureB(
      |  override val isFallbackValue: Boolean = false
      |) : Feature<FeatureB> {
      |  First(isFallbackValue = true),
      |
      |  Second;
      |}
    """.trimMargin("|")
  }

  "fails for features with no values" {
    val fixture = "feature-values-missing".toFixture()

    val result = gradleRunner.withProjectDir(fixture).buildAndFail()

    result.task(":generateFeatureFlags")!!.outcome shouldBe FAILED
    result.output shouldContain NoFeatureValues("Feature").message

    val feature = fixture.featureFile("Feature")
    feature.shouldNotExist()
  }

  "fails for features with colliding values" {
    val fixture = "feature-values-colliding".toFixture()

    val result = gradleRunner.withProjectDir(fixture).buildAndFail()

    result.task(":generateFeatureFlags")!!.outcome shouldBe FAILED
    result.output shouldContain FeatureValuesCollision("First".nel(), "Feature").message

    val feature = fixture.featureFile("Feature")
    feature.shouldNotExist()
  }

  "fails for features with corrupted values" {
    val fixture = "feature-values-corrupted".toFixture()

    val result = gradleRunner.withProjectDir(fixture).buildAndFail()

    result.task(":generateFeatureFlags")!!.outcome shouldBe FAILED
    result.output shouldContain InvalidFeatureValues(Nel("!!!, ???"), "Feature").message

    val feature = fixture.featureFile("Feature")
    feature.shouldNotExist()
  }

  "fails for features with corrupted names" {
    val fixture = "feature-name-corrupted".toFixture()

    val result = gradleRunner.withProjectDir(fixture).buildAndFail()

    result.task(":generateFeatureFlags")!!.outcome shouldBe FAILED
    result.output shouldContain InvalidFeatureName("!!!", "!!!").message

    val feature = fixture.featureFile("!!!")
    feature.shouldNotExist()
  }

  "fails for features with corrupted package names" {
    val fixture = "feature-package-name-corrupted".toFixture()

    val result = gradleRunner.withProjectDir(fixture).buildAndFail()

    result.task(":generateFeatureFlags")!!.outcome shouldBe FAILED
    result.output shouldContain InvalidPackageName("!!!.Feature").message

    val feature = fixture.featureFile("!!!.Feature")
    feature.shouldNotExist()
  }

  "fails for features with colliding namespaces" {
    val fixture = "feature-namespace-colliding".toFixture()

    val result = gradleRunner.withProjectDir(fixture).buildAndFail()

    result.task(":generateFeatureFlags")!!.outcome shouldBe FAILED
    result.output shouldContain FeaturesCollision("io.mehow.Feature".nel()).message

    val feature = fixture.featureFile("io.mehow.Feature")
    feature.shouldNotExist()
  }

  "generates feature flag for Android project" {
    val fixture = "feature-android-smoke".toFixture()

    val result = gradleRunner.withProjectDir(fixture).build()

    result.task(":generateFeatureFlags")!!.outcome shouldBe SUCCESS

    val feature = fixture.featureFile("Feature")
    feature.shouldExist()

    feature.readText() shouldContain """
      |enum class Feature(
      |  override val isFallbackValue: Boolean = false
      |) : io.mehow.laboratory.Feature<Feature> {
      |  First(isFallbackValue = true);
      |}
    """.trimMargin("|")
  }
})
