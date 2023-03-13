package bassamalim.ser.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import bassamalim.ser.core.ui.theme.AppTheme

@Composable
fun MyParentColumn(
    modifier: Modifier = Modifier,
    scroll: Boolean = true,
    content: @Composable ColumnScope.() -> Unit
) {
    if (scroll) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(AppTheme.colors.background),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            content()
        }
    }
    else {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(AppTheme.colors.background),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            content()
        }
    }
}

@Composable
fun MyColumn(
    modifier: Modifier = Modifier,
    alignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    arrangement: Arrangement.Vertical = Arrangement.SpaceEvenly,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        verticalArrangement = arrangement,
        horizontalAlignment = alignment,
        modifier = modifier.fillMaxWidth()
    ) {
        content()
    }
}

@Composable
fun MyRow(
    modifier: Modifier = Modifier,
    alignment: Alignment.Vertical = Alignment.CenterVertically,
    arrangement: Arrangement.Horizontal = Arrangement.SpaceEvenly,
    padding: PaddingValues = PaddingValues(horizontal = 10.dp),
    content: @Composable RowScope.() -> Unit
) {
    Row(
        verticalAlignment = alignment,
        horizontalArrangement = arrangement,
        modifier = modifier
            .fillMaxWidth()
            .padding(padding)
    ) {
        content()
    }
}