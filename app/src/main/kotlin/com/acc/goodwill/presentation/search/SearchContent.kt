package com.acc.goodwill.presentation.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.acc.common.ui.largePadding
import com.acc.common.ui.smallerPadding
import com.acc.goodwill.domain.model.Contributor
import com.acc.goodwill.presentation.common.SearchContributorResultUI
import java.awt.Color
import java.awt.Dimension
import javax.swing.JPanel
import javax.swing.JTextField

private val searchFiled: JTextField = JTextField("")

@Composable
fun SearchContent(
    searchKeyword: (String) -> Unit,
    selectedContributor: (Contributor) -> Unit,
    searchResults: List<Contributor>
) {
    val rgb: Int = MaterialTheme.colors.background.toArgb()
    val density = LocalDensity.current
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
    ) {
        Column(
            modifier = Modifier.padding(largePadding)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(40.dp)
                        .onSizeChanged {
                            var width: Int
                            var height: Int
                            with(density) {
                                width = it.width.toDp().value.toInt()
                                height = it.height.toDp().value.toInt()
                            }
                            searchFiled.preferredSize = Dimension(width, height)
                        }
                ) {
                    SwingPanel(
                        factory = {
                            JPanel().apply {
                                background = Color(rgb)
                                add(searchFiled.apply {
                                    addActionListener {
                                        if (searchFiled.text.isNotEmpty()) searchKeyword(searchFiled.text)
                                    }
                                })
                            }
                        }
                    )
                }

                Spacer(modifier = Modifier.width(smallerPadding))
                Button(
                    onClick = { if (searchFiled.text.isNotEmpty()) searchKeyword(searchFiled.text) },
                ) {
                    Text("검색")
                }
            }
            Text(
                text = "이름 또는 번호로 검색하기",
                style = MaterialTheme.typography.caption,
            )

            Spacer(modifier = Modifier.height(30.dp))

            SearchContributorResultUI(searchResults, selectedContributor)
        }
    }
}
