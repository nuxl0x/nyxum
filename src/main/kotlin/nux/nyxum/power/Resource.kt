package nux.nyxum.power

import com.google.gson.annotations.SerializedName

data class Resource(
    @SerializedName("min_value")
    val minValue: Int = 0,
    @SerializedName("max_value")
    val maxValue: Int = 100,
    var value: Int = maxValue,
    @SerializedName("should_render")
    val shouldRender: Boolean = false
)
