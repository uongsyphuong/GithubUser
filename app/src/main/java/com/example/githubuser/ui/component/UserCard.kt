package com.example.githubuser.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.githubuser.ui.uistate.UserDetailsUiModel
import com.example.githubuser.utils.AppDimension

@Composable
fun UserCard(uiModel: UserDetailsUiModel, onClickItem: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(AppDimension.screenPaddingDefault)
            .clickable {
                onClickItem.invoke(uiModel.username)
            },
        elevation = CardDefaults.cardElevation(defaultElevation = AppDimension.elevationDefault)
    ) {
        Row(
            modifier = Modifier
                .padding(AppDimension.screenPaddingDefault)
                .fillMaxWidth(),
        ) {

            Box(
                modifier = Modifier
                    .size(AppDimension.avatarSize)
                    .clip(RoundedCornerShape(AppDimension.padding8))
                    .background(Color.Gray.copy(alpha = 0.2f)),
            ) {
                AsyncImage(
                    modifier = Modifier
                        .size(AppDimension.avatarSize)
                        .clip(CircleShape),
                    model = ImageRequest.Builder(LocalContext.current).data(uiModel.imageUrl)
                        .build(),
                    contentDescription = "",
                    contentScale = ContentScale.Crop
                )
            }
            val uriHandler = LocalUriHandler.current

            Spacer(modifier = Modifier.width(AppDimension.screenPaddingDefault))
            Column {
                Text(text = uiModel.username, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(AppDimension.padding8))
                Divider(modifier = Modifier.height(AppDimension.dividerHeight))
                Spacer(modifier = Modifier.height(AppDimension.padding8))
                Text(text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontStyle = FontStyle.Italic,
                            textDecoration = TextDecoration.Underline
                        )
                    ) {
                        append(uiModel.url)
                    }
                },
                    color = Color.Blue,
                    modifier = Modifier.clickable { uriHandler.openUri(uiModel.url) })
            }
        }
    }
}