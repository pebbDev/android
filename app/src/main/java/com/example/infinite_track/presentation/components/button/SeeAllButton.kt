package com.example.infinite_track.presentation.components.button

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.infinite_track.R
import com.example.infinite_track.presentation.core.body1
import com.example.infinite_track.presentation.core.body2
import com.example.infinite_track.presentation.theme.Blue_500
import com.example.infinite_track.presentation.theme.Purple_500


@Composable
fun SeeAllButton(
    label: String,
    onClickButton: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = label,
            style = body1,
            color = Purple_500,

            )
        Text(
            text = stringResource(R.string.see_more),
            style = body2,
            color = Blue_500,
            modifier = Modifier
                .clickable {
                    onClickButton()
                }
        )
    }

}

@Preview(showBackground = true)
@Composable
fun SeeAllButtonPreview() {
    SeeAllButton(label = stringResource(R.string.today_s_news)) {
    }
}