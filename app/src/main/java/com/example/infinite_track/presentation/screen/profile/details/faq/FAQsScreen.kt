import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.infinite_track.R
import com.example.infinite_track.domain.model.profile.FAQCategory
import com.example.infinite_track.domain.model.profile.faq
import com.example.infinite_track.presentation.components.button.InfiniteTracButtonBack
import com.example.infinite_track.presentation.components.button.InfiniteTrackButton
import com.example.infinite_track.presentation.core.body1
import com.example.infinite_track.presentation.core.headline4
import com.example.infinite_track.presentation.theme.Infinite_TrackTheme

@Composable
fun FAQCategoryList(
    categories: List<FAQCategory>,
    onBackClick: () -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Transparent,
        topBar = {
            InfiniteTracButtonBack(
                title = "Frequently Asked Questions",
                navigationBack = onBackClick,
                modifier = Modifier.padding(top = 12.dp)
            )
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(Modifier.height(12.dp))
                categories.forEach { category ->
                    FAQCategory(category = category)
                    Divider(
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                InfiniteTrackButton(
                    modifier = Modifier
                        .padding(20.dp),
                    onClick = { /*TODO*/ },
                    label = stringResource(R.string.submit_question_label)
                )

                Spacer(modifier = Modifier.height(192.dp))

            }
        }
    }
}


@Composable
fun FAQCategory(category: FAQCategory) {
    var categoryExpanded by remember { mutableStateOf(false) }

    val rotationAngle by animateFloatAsState(if (categoryExpanded) 180f else 0f)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { categoryExpanded = !categoryExpanded }
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .border(1.dp, Color.White, shape = RoundedCornerShape(12.dp))
                .background(Color(0x80D6D6D6), shape = RoundedCornerShape(12.dp))
                .fillMaxWidth()
                .height(44.dp),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            Text(
                text = category.title,
                style = headline4,
                fontWeight = SemiBold,
                modifier = Modifier
                    .padding(start = 16.dp)
                    .weight(1f)
            )
            Icon(
                painter = painterResource(R.drawable.ic_dropdown),
                contentDescription = "Dropdown Icon",
                tint = Color.Gray,
                modifier = Modifier
                    .size(24.dp)
                    .padding(end = 8.dp)
                    .rotate(rotationAngle)
            )
        }
        AnimatedVisibility(visible = categoryExpanded) {
            Column(modifier = Modifier.padding(start = 16.dp)) {
                category.faqs.forEach { (question, answer) ->
                    FAQItem(question = question, answer = answer)
                }
            }
        }
    }
}

@Composable
fun FAQItem(question: String, answer: String) {
    var questionExpanded by remember { mutableStateOf(false) }

    val rotationAngle by animateFloatAsState(if (questionExpanded) 180f else 0f)

    Column(
        modifier = Modifier
            .heightIn(min = 39.dp)
            .clickable { questionExpanded = !questionExpanded }
            .padding(start = 32.dp, top = 4.dp, bottom = 4.dp)
            .border(1.dp, Color.White, shape = RoundedCornerShape(12.dp))
            .background(Color(0x80F3ECFF), shape = RoundedCornerShape(12.dp))
            .width(290.dp)
            .padding(horizontal = 8.dp, vertical = 8.dp)
    ) {
        Row(
            //modifier = Modifier.height(39.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = question, style = body1,
                modifier = Modifier.weight(1f)
            )
            Icon(
                painter = painterResource(R.drawable.ic_dropdown),
                contentDescription = "Dropdown Icon",
                tint = Color.Gray,
                modifier = Modifier
                    .size(20.dp)
                    .padding(end = 8.dp)
                    .rotate(rotationAngle)
            )
        }
        AnimatedVisibility(visible = questionExpanded) {
            Text(
                text = answer,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FAQCategoryListPreview() {
    Infinite_TrackTheme() {
        FAQCategoryList(
            onBackClick = {},
            categories = listOf(
                FAQCategory(
                    title = "Attendance Related Questions",
                    faqs = listOf(
                        faq(
                            question = "How do I  mark my attendance? ",
                            answer = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua",
                        ),
                        faq(
                            question = "What if I am late for work?",
                            answer = "If you arrive late, the app will still allow you to check In. However, your attendance record will be marked as late. ",
                        )
                    ),
                ),
                FAQCategory(
                    title = "System related questions",
                    faqs = listOf(
                        faq(
                            question = "Lorep Ipsum?",
                            answer = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua"
                        ),
                        faq(
                            "Loremmm? ipsum!",
                            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua"
                        )
                    ),
                ),
                FAQCategory(
                    title = "Support related questions",
                    faqs = listOf(
                        faq(
                            question = "Lorep Ipsum?",
                            answer = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua"
                        ),
                        faq(
                            "Loremmm? ipsum!",
                            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua"
                        )
                    ),
                )
            )
        )
    }
}
