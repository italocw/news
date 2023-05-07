package com.example.news.ui.newsdetail

import android.widget.TextView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage
import com.example.news.R
import com.example.news.domain.News
import com.example.news.toProperlyEncoding
import com.google.accompanist.themeadapter.material.MdcTheme

@Composable
fun NewsDetailDescription(news: News) {
    MdcTheme() {
        NewsDetailContent(news)
    }
}

@Composable
fun NewsDetailContent(news: News) {

    Surface {
        Column(
            Modifier
                .fillMaxHeight()
                .verticalScroll((rememberScrollState()))
                .padding(dimensionResource(id = R.dimen.margin))
        ) {
            news.apply {
                NewsImage(urlToImage!!)
                NewsTitle(title!!)
                NewsContentText(content!!)
                NewsAuthor(author!!)
                NewsUrl(url)
            }
        }
    }
}

@Composable
private fun NewsImage(urlToImage: String) {
    val placeholder = R.drawable.news_image_placeholder

    AsyncImage(
        model = urlToImage,
        contentScale = ContentScale.FillWidth,
        error = painterResource(placeholder),
        placeholder = painterResource(placeholder),
        contentDescription = stringResource(R.string.content_description_media),
        modifier = Modifier.fillMaxWidth()
    )
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
        }.toProperlyEncoding()
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
        NewsContentText("Senador se defende e abre processo")
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
        NewsAuthor("Fred")
    }
}

@Composable
private fun NewsUrl(sourceName: String) {
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
private fun NewsUrlPreview() {
    MdcTheme {
        NewsUrl("http://www.noticas.com.br")
    }
}


