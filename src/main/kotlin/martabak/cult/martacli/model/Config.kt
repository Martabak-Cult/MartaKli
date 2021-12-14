

data class Config(
    val layers: Map<String, String>,
    val resolution: Resolution,
    val metadata: MetadataConfig
)

data class Resolution(
    val width: Int = 1800,
    val height: Int = 1800
)

data class MetadataConfig(
    val namePrefix: String,
    val description: String
)