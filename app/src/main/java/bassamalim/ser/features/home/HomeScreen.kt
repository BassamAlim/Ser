package bassamalim.ser.features.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import bassamalim.ser.R
import bassamalim.ser.core.ui.components.ExpandableCard
import bassamalim.ser.core.ui.components.MyColumn
import bassamalim.ser.core.ui.components.MyText

@Composable
fun HomeUI() {
    MyColumn {
        ExpandableCard(
            title = stringResource(R.string.aes),
            modifier = Modifier.padding(top = 10.dp),
            innerPadding = PaddingValues(vertical = 10.dp),
            expandedContent = {
                MyText(stringResource(R.string.aes_description))
            }
        )

        ExpandableCard(
            title = stringResource(R.string.rsa),
            modifier = Modifier.padding(top = 4.dp),
            innerPadding = PaddingValues(vertical = 10.dp),
            expandedContent = {
                MyText(stringResource(R.string.rsa_description))
            }
        )
    }
}