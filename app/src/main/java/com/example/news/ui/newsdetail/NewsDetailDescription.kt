package com.example.news.ui.newsdetail

import android.widget.TextView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.news.R
import com.example.news.domain.News
import com.google.accompanist.themeadapter.material.MdcTheme
import java.time.LocalDateTime

@Composable
fun NewsDetailDescription(news: News) {
    NewsDetailContent(news)
}

@Composable
fun NewsDetailContent(news: News) {
    Surface {
        Column(Modifier.fillMaxHeight()) {
            news.apply {
                NewsImage(urlToImage!!)
                NewsTitle(title!!)
                NewsContentText(content!!)
                NewsAuthor(author!!)
                NewsSourceName(sourceName)
            }
        }
    }
}

@Preview
@Composable
private fun NewsDetailContentPreview() {
    val news = News(
        "Motorista morre em acidente",
        "www.google.com",
        "https://img.olhardigital.com.br/wp-content/uploads/2023/04/Destaque-Bitcoin.jpg",
        "descrição",
        "uol",
        "José",
        LocalDateTime.now(),
        "Conteúdo de grande qualidade"
    )
    MaterialTheme {
        NewsDetailContent(news)
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun NewsImage(urlToImage: String) {
    val imageHeight = dimensionResource(R.dimen.news_preview_image_height)

    GlideImage(
        model = urlToImage,
        contentDescription = stringResource(R.string.content_description_media),
        modifier = Modifier
            .height(imageHeight)
            .fillMaxWidth()
    )
}

@Preview
@Composable
private fun NewsImagePreview() {
    MdcTheme {
        NewsImage("https://img.olhardigital.com.br/wp-content/uploads/2023/04/Destaque-Bitcoin.jpg")
    }
}

@Composable
private fun NewsTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.h6,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = dimensionResource(R.dimen.margin)),
        color = colorResource(id = androidx.appcompat.R.color.primary_text_default_material_dark)
    )
}

@Preview
@Composable
private fun NewsTitlePreview() {
    MdcTheme {
        NewsTitle("Xuxa acusa senador")
    }
}

@Composable
private fun NewsContentText(textContent: String) {

    val treatedContentText = remember(textContent) {
        if (textContent.length >= 200) {
            "${textContent.subSequence(0, 199)}..."
        } else {
            textContent
        }
    }

    AndroidView(
        factory = { context ->
            return@AndroidView TextView(context).apply {
                setTextAppearance(androidx.appcompat.R.style.TextAppearance_AppCompat_Body1)
            }
        },
        update = {
            it.text = treatedContentText
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = dimensionResource(R.dimen.half_margin))
    )
}


@Preview
@Composable
private fun NewsContentTextPreview() {
    MdcTheme {
        NewsContentText("Xuxa acusa senador")
    }
}

@Composable
private fun NewsAuthor(author: String) {
    Text(
        text = author,
        style = MaterialTheme.typography.subtitle2,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = dimensionResource(R.dimen.half_margin)),
        color = colorResource(id = com.google.android.material.R.color.secondary_text_default_material_dark)
    )
}

@Preview
@Composable
private fun NewsAuthorPreview() {
    MdcTheme {
        NewsAuthor("Xuxa acusa senador")
    }
}

@Composable
private fun NewsSourceName(sourceName: String) {
    Text(
        text = sourceName,
        style = MaterialTheme.typography.subtitle2,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = dimensionResource(R.dimen.half_margin),
            ),
        color = colorResource(id = com.google.android.material.R.color.secondary_text_default_material_dark)
    )
}

@Preview
@Composable
private fun NewsSourceNamePreview() {
    MdcTheme {
        NewsSourceName("Xuxa acusa senador")
    }
}


