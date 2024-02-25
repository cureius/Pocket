package com.cureius.pocket.feature_account.presentation.account.components

import android.graphics.RuntimeShader
import android.os.Build
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cureius.pocket.R
import com.cureius.pocket.feature_account.domain.model.Bank
import org.intellij.lang.annotations.Language


@Language("AGSL")
val CUSTOM_SHADER = """
    uniform float2 resolution;
    layout(color) uniform half4 color;
    layout(color) uniform half4 color2;

    half4 main(in float2 fragCoord) {
        float2 uv = fragCoord/resolution.xy;

        float mixValue = distance(uv, vec2(0, 1));
        return mix(color, color2, mixValue);
    }
""".trimIndent()

@Composable
fun AccountItem(
    bankName: String, cardNumber: String, accountNumber: String, holderName: String
) {
    val banks = listOf(
        Bank(
            icon = painterResource(id = R.drawable.pnb), name = "PNB"
        ), Bank(
            icon = painterResource(id = R.drawable.hdfc), name = "HDFC"
        ), Bank(
            icon = painterResource(id = R.drawable.kotak), name = "Kotak"
        ), Bank(
            icon = painterResource(id = R.drawable.sbi), name = "SBI"
        )
    )
    val aspectRatio = 1.58f // Set the aspect ratio as desired

    val paddingModifier = Modifier

    val Coral = Color(0xFFA897F3)
    val LightYellow = Color(0xFFF8EE94)

    BoxWithRoundedCorners(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .aspectRatio(aspectRatio),
    ) {
        Box(
            modifier = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                Modifier
                    .drawWithCache {
                        val shader = RuntimeShader(CUSTOM_SHADER)

                        val shaderBrush = ShaderBrush(shader)
                        shader.setFloatUniform("resolution", size.width, size.height)
                        onDrawBehind {
                            shader.setColorUniform(
                                "color", android.graphics.Color.valueOf(
                                    LightYellow.red,
                                    LightYellow.green,
                                    LightYellow.blue,
                                    LightYellow.alpha
                                )
                            )
                            shader.setColorUniform(
                                "color2", android.graphics.Color.valueOf(
                                    Coral.red, Coral.green, Coral.blue, Coral.alpha
                                )
                            )
                            drawRect(shaderBrush)
                        }
                    }
            } else {
                Modifier.background(MaterialTheme.colors.primary.copy(0.4f), RoundedCornerShape(16.dp))
            }

        ) {
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colors.background.copy(alpha = 0.1f))
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row {
                        val borderWidth = 2.dp
                        println(bankName.lowercase())
                        var bankIcon = banks.first {
                            println(it.name!!.lowercase())
                            bankName.lowercase().contains(
                                it.name!!.lowercase()
                            )
                        }.icon
                        println(bankIcon)
                        Image(
                            painter = bankIcon ?: painterResource(id = R.drawable.accounts),
                            contentDescription = "Bank Logo",
                            contentScale = ContentScale.Inside,
                            modifier = Modifier
                                .size(36.dp)
                                .border(
                                    BorderStroke(borderWidth, MaterialTheme.colors.primary),
                                    CircleShape
                                )
                                .padding(borderWidth)
                                .clip(CircleShape),
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Column() {
                            Text(
                                text = bankName,
                                color = Color.Black,
                                textAlign = TextAlign.Center,
                                style = TextStyle(fontWeight = FontWeight.Bold),
                                fontSize = 16.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.padding(4.dp, 0.dp)
                            )
                            Text(
                                text = "Bank Account",
                                color = Color.Black,
                                textAlign = TextAlign.Center,
                                style = TextStyle(fontWeight = FontWeight.Bold),
                                fontSize = 8.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.padding(4.dp, 0.dp)
                            )
                        }

                    }
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val pngImage: Painter = painterResource(id = R.drawable.chip)
                        Image(
                            painter = pngImage,
                            contentDescription = "smart chip",
                            modifier = Modifier.size(110.dp),
                        )
                        Column {
                            Text(
                                text = "XXXX XXXX XXXX ${
                                    if (cardNumber != "") {
                                        cardNumber
                                    } else {
                                        "XXXX"
                                    }
                                }",
                                color = Color.Black,
                                textAlign = TextAlign.Center,
                                style = TextStyle(fontWeight = FontWeight.Bold),
                                fontSize = 18.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.padding(4.dp, 0.dp)
                            )
                            Text(
                                text = "**********$accountNumber",
                                color = Color.Black,
                                textAlign = TextAlign.Center,
                                style = TextStyle(
                                    fontWeight = FontWeight.Normal, letterSpacing = 3.5.sp
                                ),
                                fontSize = 16.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.padding(4.dp, 0.dp)
                            )
                        }
                    }
                    Text(
                        text = holderName.uppercase(),
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        style = TextStyle(fontWeight = FontWeight.Bold),
                        fontSize = 26.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(4.dp, 0.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun BoxWithRoundedCorners(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 16.dp,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier.clip(RoundedCornerShape(cornerRadius)),
    ) {
        content()
    }
}

@Preview
@Composable
fun AccountItemPreview() {
    AccountItem(
        bankName = "HDFC", cardNumber = "1234", accountNumber = "890", holderName = "John Doe"
    )
}