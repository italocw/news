package com.example.supportactivityhelper

import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.databinding.BindingAdapter
import com.example.news.layout.main.TicketListScreenStatus
import com.example.news.repository.FetchingState


@BindingAdapter("key")
fun TextView.setKey(ticket: Ticket?) {
    ticket?.let {
        text = ticket.key
    }
}

@BindingAdapter("key_and_summary")
fun TextView.setKeyAndSummary(ticket: Ticket?) {
    ticket?.let {
        text = "${ticket.key} - ${ticket.summary} "
    }
}

@BindingAdapter("last_public_comment_author")
fun TextView.setLastPublicCommentAuthor(ticket: Ticket?) {
    ticket?.let {
        val lastPublicCommentAuthorName = ticket.lastPublicComment!!.author.authorName
        val commentedByText = context.getString(R.string.commented_by)
        val commentedByFormatted = "$commentedByText <b>$lastPublicCommentAuthorName</b>"

        text = HtmlCompat.fromHtml(commentedByFormatted, HtmlCompat.FROM_HTML_MODE_LEGACY);
    }
}

@BindingAdapter("summary")
fun TextView.setSummary(ticket: Ticket?) {
    ticket?.let {
        text = ticket.summary
    }
}


@BindingAdapter("dataFetchingStatus")
fun TextView.setFetchingState(dataFetchingStatus: FetchingState?) {
    if (dataFetchingStatus == FetchingState.DOWNLOADING_NEWS) {
        text = resources.getText(R.string.getting_jira_tickets)

    } else if (dataFetchingStatus == FetchingState.CHECKING_UP_COMMENTS) {
        text = resources.getText(R.string.checking_up_comments)
    }
}

@BindingAdapter("statusInformationMessage")
fun TextView.setStatusInformationMessage(ticketListScreenStatus: TicketListScreenStatus) {

    if (ticketListScreenStatus == TicketListScreenStatus.CONNECTION_PROBLEM) {
        text = resources.getText(R.string.internet_connection_not_available)
    } else if (ticketListScreenStatus == TicketListScreenStatus.EMPTY_LIST) {
        text = resources.getText(R.string.empty_ticket_list_text)
    } else if (ticketListScreenStatus == TicketListScreenStatus.ERROR) {
        text = resources.getText(R.string.an_error_has_ocurring_when_trying_to_get_tickets)
    }
}


@BindingAdapter("comment")
fun TextView.comment(ticket: Ticket?) {
    ticket?.let {
        text = ticket.lastPublicComment!!.message
    }
}