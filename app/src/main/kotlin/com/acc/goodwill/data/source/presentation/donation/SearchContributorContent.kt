package com.acc.goodwill.data.source.presentation.donation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.awtEventOrNull
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.acc.common.components.AppIcon
import com.acc.common.ui.largePadding
import com.acc.common.ui.mediumPadding
import com.acc.common.ui.smallPadding
import com.acc.common.ui.smallerPadding
import com.acc.di.AppComponent
import com.acc.goodwill.domain.model.Contributor
import javax.inject.Inject

class SearchContributorContent(appComponent: AppComponent) {
    @Inject lateinit var viewModel: DonationViewModel

    init {
        appComponent.inject(this)
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun SearchContributorContent(
        navigateAddContributor: () -> Unit,
        selectedContributor: (Contributor) -> Unit,
        searchResult: List<Contributor>,
        searchKeyword: (String) -> Unit
    ) {

        var keyword by remember { mutableStateOf("") }
        var selectedIndex by remember { mutableStateOf(-1) }

        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            floatingActionButton = {
                FloatingActionButton(onClick = navigateAddContributor) {
                    AppIcon(imageVector = Icons.Default.Add)
                }
            }
        ) {
            Column(modifier = Modifier.padding(start = largePadding, end = largePadding)) {
                Box(
                    Modifier
                        .requiredHeight(80.dp)
                        .align(Alignment.Start)
                        .padding(top = largePadding)
                ) {
                    Button(onClick = { selectedContributor(Contributor.UNKNOWN) }) {
                        Text("무명으로 등록하기")
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    BasicTextField(
                        value = keyword,
                        onValueChange = { keyword = it },
                        enabled = true,
                        singleLine = true,
                        textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colors.onBackground),
                        modifier = Modifier
                            .border(
                                width = 1.25.dp,
                                color = MaterialTheme.colors.primary.copy(alpha = 0.7f),
                                shape = RoundedCornerShape(4.dp)
                            )
                            .padding(smallPadding)
                            .weight(0.3f)
                            .onKeyEvent { keyEvent ->
                                if (keyEvent.key != Key.Enter) return@onKeyEvent false
                                if (keyEvent.type == KeyEventType.KeyUp && keyword.isNotEmpty()) {
                                    searchKeyword(keyword)
                                    true
                                } else {
                                    false
                                }
                            }
                    )
                    Spacer(modifier = Modifier.width(smallerPadding))
                    Button(
                        onClick = { searchKeyword(keyword) },
                        enabled = keyword.isNotEmpty()
                    ) {
                        Text("검색")
                    }
                }
                Text(
                    text = "이름 또는 번호로 검색하기",
                    style = MaterialTheme.typography.caption,
                )

                Spacer(modifier = Modifier.height(30.dp))

                Text("검색 결과")
                Spacer(modifier = Modifier.height(smallerPadding))

                LazyScrollable(
                    searchResult = searchResult,
                    selectedIndex = selectedIndex,
                    setValue = { selectedIndex = if (selectedIndex != it) it else -1 }
                )

                Row(Modifier.weight(0.1f)) {
                    if (selectedIndex != -1) {
                        Button(onClick = {}) {
                            Text("수정하기")
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Button(onClick = { selectedContributor(searchResult[selectedIndex]) }) {
                            Text("다음")
                        }
                    }
                }
            }

        }
    }

    @Composable
    private fun LazyScrollable(
        searchResult: List<Contributor>,
        selectedIndex: Int,
        setValue: (Int) -> Unit,
        state: LazyListState = rememberLazyListState()
    ) {

        if (searchResult.isEmpty()) {
            Text("검색 결과가 없습니다.")
            return
        }

        Box(
            modifier = Modifier.fillMaxHeight(fraction = 0.8f).padding(10.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(end = 12.dp)
                    .scrollbar(
                        state,
                        horizontal = false,
                        trackColor = Color.Red,
                        knobColor = Color.Yellow
                    ),
                state = state,
            ) {
                items(searchResult.size) { index ->
                    val contributor = searchResult[index]
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(if (selectedIndex == index) MaterialTheme.colors.primary else Color.White)
                            .selectable(
                                selected = index == selectedIndex,
                                onClick = { setValue(index) }
                            )
                            .padding(mediumPadding)
                    ) {
                        Column {
                            Text("이름 : ${contributor.name}")
                            Text("번호 : ${contributor.phoneNumber}")
                            Text("주민/사업자 번호 : ${contributor.registrationNumber}")
                        }
                    }
                    Spacer(modifier = Modifier.height(largePadding))
                }
            }
            VerticalScrollbar(
                modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
                adapter = rememberScrollbarAdapter(scrollState = state)
            )
        }
    }

}


@Composable
fun Modifier.scrollbar(
    state: LazyListState,
    horizontal: Boolean,
    alignEnd: Boolean = true,
    thickness: Dp = 4.dp,
    fixedKnobRatio: Float? = null,
    knobCornerRadius: Dp = 4.dp,
    trackCornerRadius: Dp = 2.dp,
    knobColor: Color = Color.Black,
    trackColor: Color = Color.White,
    padding: Dp = 0.dp,
    visibleAlpha: Float = 1f,
    hiddenAlpha: Float = 0f,
    fadeInAnimationDurationMs: Int = 150,
    fadeOutAnimationDurationMs: Int = 500,
    fadeOutAnimationDelayMs: Int = 1000,
): Modifier {
    check(thickness > 0.dp) { "Thickness must be a positive integer." }
    check(fixedKnobRatio == null || fixedKnobRatio < 1f) {
        "A fixed knob ratio must be smaller than 1."
    }
    check(knobCornerRadius >= 0.dp) { "Knob corner radius must be greater than or equal to 0." }
    check(trackCornerRadius >= 0.dp) { "Track corner radius must be greater than or equal to 0." }
    check(hiddenAlpha <= visibleAlpha) { "Hidden alpha cannot be greater than visible alpha." }
    check(fadeInAnimationDurationMs >= 0) {
        "Fade in animation duration must be greater than or equal to 0."
    }
    check(fadeOutAnimationDurationMs >= 0) {
        "Fade out animation duration must be greater than or equal to 0."
    }
    check(fadeOutAnimationDelayMs >= 0) {
        "Fade out animation delay must be greater than or equal to 0."
    }

    val targetAlpha =
        if (state.isScrollInProgress) {
            visibleAlpha
        } else {
            hiddenAlpha
        }
    val animationDurationMs =
        if (state.isScrollInProgress) {
            fadeInAnimationDurationMs
        } else {
            fadeOutAnimationDurationMs
        }
    val animationDelayMs =
        if (state.isScrollInProgress) {
            0
        } else {
            fadeOutAnimationDelayMs
        }

    val alpha by
    animateFloatAsState(
        targetValue = targetAlpha,
        animationSpec =
        tween(delayMillis = animationDelayMs, durationMillis = animationDurationMs))

    return drawWithContent {
        drawContent()

        state.layoutInfo.visibleItemsInfo.firstOrNull()?.let { firstVisibleItem ->
            if (state.isScrollInProgress || alpha > 0f) {
                // Size of the viewport, the entire size of the scrollable composable we are decorating with
                // this scrollbar.
                val viewportSize =
                    if (horizontal) {
                        size.width
                    } else {
                        size.height
                    } - padding.toPx() * 2

                // The size of the first visible item. We use this to estimate how many items can fit in the
                // viewport. Of course, this works perfectly when all items have the same size. When they
                // don't, the scrollbar knob size will grow and shrink as we scroll.
                val firstItemSize = firstVisibleItem.size

                // The *estimated* size of the entire scrollable composable, as if it's all on screen at
                // once. It is estimated because it's possible that the size of the first visible item does
                // not represent the size of other items. This will cause the scrollbar knob size to grow
                // and shrink as we scroll, if the item sizes are not uniform.
                val estimatedFullListSize = firstItemSize * state.layoutInfo.totalItemsCount

                // The difference in position between the first pixels visible in our viewport as we scroll
                // and the top of the fully-populated scrollable composable, if it were to show all the
                // items at once. At first, the value is 0 since we start all the way to the top (or start
                // edge). As we scroll down (or towards the end), this number will grow.
                val viewportOffsetInFullListSpace =
                    state.firstVisibleItemIndex * firstItemSize + state.firstVisibleItemScrollOffset

                // Where we should render the knob in our composable.
                val knobPosition =
                    (viewportSize / estimatedFullListSize) * viewportOffsetInFullListSpace + padding.toPx()
                // How large should the knob be.
                val knobSize =
                    fixedKnobRatio?.let { it * viewportSize } ?: ((viewportSize * viewportSize) / estimatedFullListSize)

                // Draw the track
                drawRoundRect(
                    color = trackColor,
                    topLeft =
                    when {
                        // When the scrollbar is horizontal and aligned to the bottom:
                        horizontal && alignEnd -> Offset(padding.toPx(), size.height - thickness.toPx())
                        // When the scrollbar is horizontal and aligned to the top:
                        horizontal && !alignEnd -> Offset(padding.toPx(), 0f)
                        // When the scrollbar is vertical and aligned to the end:
                        alignEnd -> Offset(size.width - thickness.toPx(), padding.toPx())
                        // When the scrollbar is vertical and aligned to the start:
                        else -> Offset(0f, padding.toPx())
                    },
                    size =
                    if (horizontal) {
                        Size(size.width - padding.toPx() * 2, thickness.toPx())
                    } else {
                        Size(thickness.toPx(), size.height - padding.toPx() * 2)
                    },
                    alpha = alpha,
                    cornerRadius = CornerRadius(x = trackCornerRadius.toPx(), y = trackCornerRadius.toPx()),
                )

                // Draw the knob
                drawRoundRect(
                    color = knobColor,
                    topLeft =
                    when {
                        // When the scrollbar is horizontal and aligned to the bottom:
                        horizontal && alignEnd -> Offset(knobPosition, size.height - thickness.toPx())
                        // When the scrollbar is horizontal and aligned to the top:
                        horizontal && !alignEnd -> Offset(knobPosition, 0f)
                        // When the scrollbar is vertical and aligned to the end:
                        alignEnd -> Offset(size.width - thickness.toPx(), knobPosition)
                        // When the scrollbar is vertical and aligned to the start:
                        else -> Offset(0f, knobPosition)
                    },
                    size =
                    if (horizontal) {
                        Size(knobSize, thickness.toPx())
                    } else {
                        Size(thickness.toPx(), knobSize)
                    },
                    alpha = alpha,
                    cornerRadius = CornerRadius(x = knobCornerRadius.toPx(), y = knobCornerRadius.toPx()),
                )
            }
        }
    }
}