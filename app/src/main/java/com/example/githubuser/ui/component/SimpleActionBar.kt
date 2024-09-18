package com.example.githubuser.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.githubuser.utils.AppDimension

@Composable
fun SimpleActionBar(
    modifier: Modifier = Modifier,
    title: String = "",
    onBack: (() -> Unit)? = null
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(AppDimension.actionBarSizeDefault),
    ) {
        if (title.isNotBlank()) {
            Text(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(
                        horizontal = AppDimension.size40
                    ),
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        onBack?.let {
            Box(
                modifier = Modifier
                    .padding(AppDimension.screenPaddingDefault)
                    .size(AppDimension.size30)
                    .align(Alignment.CenterStart)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        onBack.invoke()
                    },
                contentAlignment = Alignment.CenterStart,
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "back",
                )
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun Preview() {
    SimpleActionBar(title = "Title",
        onBack = {}
    )
}