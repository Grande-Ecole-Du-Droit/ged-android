package com.upsaclay.authentication.presentation.registration.second

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.upsaclay.authentication.R
import com.upsaclay.authentication.presentation.components.RegistrationScaffold
import com.upsaclay.common.presentation.components.PrimaryButton
import com.upsaclay.common.presentation.components.SimpleDropDownMenu
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.spacing
import com.upsaclay.common.utils.Phones
import com.upsaclay.common.utils.mediumPadding
import org.koin.androidx.compose.koinViewModel

@Composable
fun SecondRegistrationScreen(
    onNextClick: (String) -> Unit,
    onBackClick: () -> Unit,
    viewModel: SecondRegistrationViewModel = koinViewModel()
) {
    val schoolLevel by viewModel.schoolLevel.collectAsState()

    SecondRegistrationScreen(
        schoolLevel = schoolLevel,
        schoolLevels = viewModel.schoolLevels,
        onItemClick = viewModel::onSchoolLevelChange,
        onNextClick = { onNextClick(schoolLevel) },
        onBackClick = onBackClick
    )
}

@Composable
private fun SecondRegistrationScreen(
    schoolLevel: String,
    schoolLevels: List<String>,
    onItemClick: (String) -> Unit,
    onNextClick: () -> Unit,
    onBackClick: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    RegistrationScaffold(
        onBackClick = onBackClick
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .mediumPadding(paddingValues)
                .pointerInput(Unit) {
                    detectTapGestures(onPress = { expanded = false })
                }
        ) {
            Column {
                Text(
                    text = stringResource(id = R.string.select_level_school),
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

                SimpleDropDownMenu(
                    items = schoolLevels,
                    selectedItem = schoolLevel,
                    onItemClicked = { item ->
                        onItemClick(item)
                        expanded = false
                    },
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            PrimaryButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .testTag(stringResource(id = R.string.registration_screen_next_button_tag)),
                text = stringResource(id = com.upsaclay.common.R.string.next),
                onClick = onNextClick
            )
        }
    }
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@Phones
@Composable
private fun SecondRegistrationScreenPreview() {
    val items = listOf("GED 1", "GED 2", "GED 3")
    var selectedItem by remember { mutableStateOf(items[0]) }

    GedoiseTheme {
        SecondRegistrationScreen(
            schoolLevel = selectedItem,
            schoolLevels = items,
            onItemClick = { selectedItem = it },
            onNextClick = {},
            onBackClick = {}
        )
    }
}