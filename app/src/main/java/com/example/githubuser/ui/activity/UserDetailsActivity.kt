package com.example.githubuser.ui.activity

import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.githubuser.R
import com.example.githubuser.ui.component.SimpleActionBar
import com.example.githubuser.ui.uistate.UserDetailsUiModel
import com.example.githubuser.utils.AppDimension
import com.example.githubuser.viewmodel.UserDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserDetailsActivity : BaseActivity() {

    override val viewModel: UserDetailsViewModel by viewModels()

    @Composable
    override fun ComposeContent() {

        val userList by viewModel.userDetails.collectAsStateWithLifecycle()

        val username = intent.getStringExtra(USERNAME) ?: ""

        LaunchedEffect(Unit) {
            viewModel.getUserDetails(username)
        }

        Column {
            SimpleActionBar(
                title = "User Details",
                onBack = {
                    onBackPressedDispatcher.onBackPressed()
                }

            )
            UserDetails(userList)
        }

    }

    @Composable
    private fun UserDetails(
        uiModel: UserDetailsUiModel
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(AppDimension.screenPaddingDefault)
        ) {

            Card(
                modifier = Modifier
                    .fillMaxWidth(),
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
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(uiModel.imageUrl)
                                .build(),
                            contentDescription = "",
                            contentScale = ContentScale.Crop
                        )
                    }
                    Spacer(modifier = Modifier.width(AppDimension.screenPaddingDefault))
                    Column {
                        Text(
                            text = uiModel.username,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(AppDimension.padding8))
                        Divider(modifier = Modifier.height(AppDimension.dividerHeight))
                        Spacer(modifier = Modifier.height(AppDimension.padding8))
                        uiModel.location?.let {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_location), // Replace with actual drawable
                                    contentDescription = "Location",
                                    modifier = Modifier.size(AppDimension.screenPaddingDefault),
                                    tint = Color.Gray
                                )
                                Spacer(modifier = Modifier.width(AppDimension.padding4))
                                Text(text = uiModel.location.toString(), color = Color.Gray)
                            }
                        }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        vertical = AppDimension.screenPaddingDefault,
                        horizontal = AppDimension.padding32
                    ),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                StatItem(
                    count = uiModel.followers.toString(),
                    label = "Follower",
                    icon = R.drawable.ic_follower
                )
                StatItem(
                    count = uiModel.following.toString(),
                    label = "Following",
                    icon = R.drawable.ic_follower
                )
            }

            // Blog Section
            Text(text = "Blog", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(AppDimension.padding8))
            Text(text = uiModel.url)
        }
    }

    @Composable
    fun StatItem(count: String, label: String, icon: Int) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = label,
                modifier = Modifier
                    .size(AppDimension.size40)
                    .background(Color.LightGray, shape = CircleShape)
                    .padding(AppDimension.padding8),
                tint = Color.DarkGray
            )
            Spacer(modifier = Modifier.height(AppDimension.padding4))
            Text(text = count, fontWeight = FontWeight.Bold)
            Text(text = label)
        }
    }


    @Preview(showBackground = true)
    @Composable
    fun ScreenPreview() {
        Column {
            SimpleActionBar(
                title = "User Details",
                onBack = {
                    onBackPressedDispatcher.onBackPressed()
                }
            )
            UserDetails(
                UserDetailsUiModel()
            )
        }
    }

    companion object {
        const val USERNAME = "USERNAME"
    }

}