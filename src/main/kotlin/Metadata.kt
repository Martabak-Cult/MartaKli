import com.squareup.moshi.Json

data class Metadataa(
    val name: String,
    val description: String,
    val image: String,
    val tokenId: Int,
    val attributes: List<Attribute>
)

data class Attribute(
    @Json(name = "trait_type")
    val traitType: String,

    @Json(name = "value")
    val value: Any
)