package com.upsaclay.gedoise.presentation.profile.account

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.userFixture
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.gold
import com.upsaclay.common.presentation.theme.previewText
import com.upsaclay.common.presentation.theme.spacing
import com.upsaclay.common.utils.Phones
import com.upsaclay.gedoise.R
import com.upsaclay.gedoise.presentation.components.AccountInfoItem

@Composable
fun AccountInfoItems(user: User) {
    val accountInfos: List<AccountInfo> = listOf(
        AccountInfo(
            stringResource(id = com.upsaclay.common.R.string.last_name),
            user.lastName
        ),
        AccountInfo(
            stringResource(id = com.upsaclay.common.R.string.first_name),
            user.firstName
        ),
        AccountInfo(
            stringResource(id = com.upsaclay.common.R.string.email),
            user.email
        ),
        AccountInfo(
            stringResource(id = com.upsaclay.common.R.string.school_level),
            user.schoolLevel
        )
    )

    Column(
        modifier = Modifier.fillMaxWidth().padding(top = MaterialTheme.spacing.smallMedium),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
    ) {
        accountInfos.forEach { accountInfo ->
            AccountInfoItem(
                accountInfo = accountInfo
            )
        }

        if (user.isMember) {
            MemberAccountInfoItem(
                modifier = Modifier
                    .testTag(stringResource(id = R.string.account_screen_member_tag))
            )
        }
    }
}

@Composable
private fun MemberAccountInfoItem(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall)
    ) {
        Text(
            text = stringResource(R.string.member),
            color = MaterialTheme.colorScheme.previewText,
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.labelLarge
        )

        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.gold,
            modifier = Modifier.size(20.dp)
        )
    }
}

data class AccountInfo(
    val label: String,
    val value: String
)

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@Phones
@Composable
private fun AccountInfoItemsPreview() {
    GedoiseTheme {
        Surface {
            AccountInfoItems(user = userFixture)
        }
    }
}