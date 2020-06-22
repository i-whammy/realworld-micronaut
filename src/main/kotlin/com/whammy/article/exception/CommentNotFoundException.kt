package com.whammy.article.exception

import java.lang.Exception

class CommentNotFoundException(override val message: String) : Exception(message)