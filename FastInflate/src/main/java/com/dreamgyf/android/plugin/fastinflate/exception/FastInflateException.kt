package com.dreamgyf.android.plugin.fastinflate.exception

import android.view.InflateException

class FastInflateException : InflateException {
    constructor() : super()
    constructor(detailMessage: String, throwable: Throwable) : super(detailMessage, throwable)
    constructor(detailMessage: String) : super(detailMessage)
    constructor(throwable: Throwable) : super(throwable)
}