package bassamalim.ser.features.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import bassamalim.ser.R
import bassamalim.ser.core.ui.components.ExpandableCard
import bassamalim.ser.core.ui.components.MyColumn
import bassamalim.ser.core.ui.components.MyText

@Composable
fun HomeUI() {

    MyColumn {
        ExpandableCard(
            titleResId = R.string.aes,
            expandedContent = {
                MyText(stringResource(R.string.aes_description))
            }
        )

        ExpandableCard(
            titleResId = R.string.rsa,
            expandedContent = {
                MyText(stringResource(R.string.rsa_description))
            }
        )
    }

}