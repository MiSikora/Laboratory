package io.mehow.laboratory.compiler

import arrow.core.Nel

interface CompilationFailure {
  val message: String
}

internal data class InvalidPackageName(
  private val fqcn: String
) : CompilationFailure {
  override val message
    get() = "Invalid package name for $fqcn."
}

internal data class InvalidFlagName(
  private val name: String,
  private val fqcn: String
) : CompilationFailure {
  override val message
    get() = "" +
        "Flag name must contain only alphanumeric characters or underscores " +
        "and must start with a letter. Found $name in $fqcn."
}

internal data class NoFlagValues(
  private val fqcn: String
) : CompilationFailure {
  override val message
    get() = "$fqcn must have at least one value."
}

internal data class InvalidFlagValues(
  private val invalidNames: Nel<String>,
  private val fqcn: String
) : CompilationFailure {
  override val message
    get() = "" +
        "Flag values must contain only alphanumeric characters or underscores " +
        "and must start with a letter. Found $invalidNames in $fqcn."
}

internal data class FlagNameCollision(
  private val collisionNames: Nel<String>,
  private val fqcn: String
) : CompilationFailure {
  override val message: String
    get() = "Found collision in values for $fqcn: $collisionNames."
}

internal data class FlagNamespaceCollision(
  private val collisionFlags: Nel<FeatureFlagModel>
) : CompilationFailure {
  override val message: String
    get() = "Found feature flags namespace collision: $collisionFlags."
}
