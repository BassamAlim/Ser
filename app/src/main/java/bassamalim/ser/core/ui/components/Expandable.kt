package bassamalim.ser.core.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bassamalim.ser.R
import bassamalim.ser.core.ui.theme.AppTheme

@Composable
fun ExpandableCard(
    title: String,
    modifier: Modifier = Modifier,
    extraVisible: @Composable () -> Unit = {},
    expandedContent: @Composable () -> Unit
) {
    var expandedState by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(if (expandedState) 180f else 0f)

    MyClickableSurface(
        modifier = modifier
            .animateContentSize(
                animationSpec = TweenSpec(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                )
            ),
        color = AppTheme.colors.primary,
        onClick = { expandedState = !expandedState }
    ) {
        MyColumn {
            MyRow(
                arrangement = Arrangement.SpaceBetween,
                padding = PaddingValues(start = 6.dp, end = 12.dp)
            ) {
                MyText(
                    text = title,
                    fontSize = 22.sp,
                    textAlign = TextAlign.Start,
                    textColor = AppTheme.colors.strongText,
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = 10.dp, bottom = 10.dp, start = 16.dp)
                )

                extraVisible()

                Box(
                    modifier = Modifier.padding(start = 20.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = stringResource(R.string.select),
                        tint = AppTheme.colors.text,
                        modifier = Modifier
                            .size(40.dp)
                            .rotate(rotationState)
                    )
                }
            }

            if (expandedState) {
                expandedContent()
            }
        }
    }
}

@Composable
fun ExpandableItem(
    title: String,
    extraVisible: @Composable () -> Unit = {},
    expandedContent: @Composable () -> Unit
) {
    var expandedState by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(if (expandedState) 180f else 0f)

    MyColumn(
        modifier = Modifier
            .animateContentSize(
                animationSpec = TweenSpec(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                )
            )
            .clickable { expandedState = !expandedState }
    ) {
        MyRow(
            Modifier.padding(all = 16.dp)
        ) {
            MyText(
                title,
                Modifier.weight(1f),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start
            )

            extraVisible()

            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = stringResource(R.string.select),
                tint = AppTheme.colors.text,
                modifier = Modifier.rotate(rotationState)
            )
        }

        if (expandedState) {
            expandedContent()
        }
    }

    MyHorizontalDivider()
}