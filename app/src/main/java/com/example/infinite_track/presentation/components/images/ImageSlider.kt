package com.example.infinite_track.presentation.components.images

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.infinite_track.data.soucre.dummy.dummySliderItems
import com.example.infinite_track.presentation.components.pager.PagerIndicatorContent
import com.example.infinite_track.presentation.core.headline4
import com.example.infinite_track.presentation.theme.Blue_500
import com.example.infinite_track.presentation.theme.Infinite_TrackTheme
import com.example.infinite_track.presentation.theme.Purple_100
import kotlinx.coroutines.delay


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageSlider() {
    val pagerState = rememberPagerState(pageCount = {
        dummySliderItems.size
    })

    Column {
        HorizontalPager(
            pageSpacing = 4.dp,
            state = pagerState,
            modifier = Modifier.padding(bottom = 6.dp)
        ) { page ->
            val item = dummySliderItems[page]

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .height(115.dp)
                    .fillMaxWidth()
            ) {
                AsyncImage(
                    model = item.imageUrl,
                    contentDescription = item.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.LightGray) // Placeholder abu-abu saat loading
                )
                Text(
                    text = item.title,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(8.dp),
                    color = Color.White,
                    style = headline4
                )
            }
        }
        PageIndicator(
            numberOfPages = pagerState.pageCount,
            selectedPage = pagerState.currentPage,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 8.dp)
        )
    }

    LaunchedEffect(pagerState.currentPage) {
        delay(4000)
        val nextPage = (pagerState.currentPage + 1) % pagerState.pageCount
        pagerState.animateScrollToPage(nextPage)
    }
}

@Composable
fun PageIndicator(
    numberOfPages: Int,
    modifier: Modifier = Modifier,
    selectedPage: Int = 0,
    selectedColor: Color = Blue_500,
    defaultColor: Color = Purple_100,
    defaultRadius: Dp = 4.dp,
    selectedRadius: Dp = 25.dp,
    space: Dp = 2.dp,
    animationDurationMillis: Int = 400
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(space),
        modifier = modifier
    ) {
        for (i in 0 until numberOfPages) {
            val isSelected = i == selectedPage
            PagerIndicatorContent(
                isSelected = isSelected,
                selectedColor = selectedColor,
                defaultColor = defaultColor,
                defaultRadius = defaultRadius,
                selectedRadius = selectedRadius,
                animationDurationMillis = animationDurationMillis
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ImageSliderPreview() {
    Infinite_TrackTheme(
        dynamicColor = false
    ) {
        ImageSlider()
    }
}