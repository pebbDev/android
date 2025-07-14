import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.CardDefaults.outlinedCardBorder
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.infinite_track.R
import com.example.infinite_track.presentation.core.body1
import com.example.infinite_track.utils.openFile

@Composable
fun FileMyLeaveCard(
    dataTitle: String,
    fileType: String,
    filePath: String
) {
    val context = LocalContext.current

    Card(
        border = outlinedCardBorder(),
        colors = cardColors(Color(0x33FFFFFF)),
        modifier = Modifier
            .width(433.dp)
            .wrapContentHeight()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = dataTitle,
                    color = Color.Gray,
                    style = body1,
                    modifier = Modifier.weight(1f)
                )

                if (fileType == "image") {
                    val bitmap = remember(filePath) {
                        BitmapFactory.decodeFile(filePath)
                    }

                    bitmap?.let {
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentDescription = "Image Preview",
                            modifier = Modifier
                                .size(80.dp)
                                .padding(8.dp)
                                .clickable {
                                    openFile(context, filePath)
                                }
                                .weight(1f)
                        )
                    } ?: run {
                        Text(
                            text = stringResource(R.string.image_not_found),
                            style = body1,
                            color = Color.Gray,
                            modifier = Modifier.weight(1f)
                        )
                    }
                } else {
                    val painter = painterResource(id = R.drawable.ic_docs)

                    Image(
                        painter = painter,
                        contentDescription = "File Icon",
                        modifier = Modifier
                            .size(100.dp)
                            .clickable {
                                openFile(context, filePath)
                            }
                            .weight(1f),
                        colorFilter = ColorFilter.tint(Color.Gray)
                    )
                }

            }
        }
    }
}

@Preview
@Composable
fun FileMyLeaveCardPreview() {
    FileMyLeaveCard(
        dataTitle = "File",
        fileType = "image",
        filePath = "/storage/emulated/0/Download/my_image.jpg"
    )
}
