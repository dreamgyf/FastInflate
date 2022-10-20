package com.dreamgyf.android.plugin.fastinflate.plugin

fun convertSysView(name: String): String {
    return when (name) {
        "AbsoluteLayout",
        "ActionMenuView",
        "AdapterViewFlipper",
        "AnalogClock",
        "AutoCompleteTextView",
        "Button",
        "CalendarView",
        "CheckBox",
        "CheckedTextView",
        "Chronometer",
        "DatePicker",
        "DialerFilter",
        "DigitalClock",
        "EditText",
        "ExpandableListView",
        "FrameLayout",
        "Gallery",
        "GridLayout",
        "GridView",
        "HorizontalScrollView",
        "ImageButton",
        "ImageSwitcher",
        "ImageView",
        "LinearLayout",
        "ListView",
        "MediaController",
        "MultiAutoCompleteTextView",
        "NumberPicker",
        "ProgressBar",
        "QuickContactBadge",
        "RadioButton",
        "RadioGroup",
        "RatingBar",
        "RelativeLayout",
        "ScrollView",
        "SearchView",
        "SeekBar",
        "SlidingDrawer",
        "Space",
        "Spinner",
        "StackView",
        "Switch",
        "TabHost",
        "TableLayout",
        "TableRow",
        "TabWidget",
        "TextClock",
        "TextSwitcher",
        "TextView",
        "TimePicker",
        "ToggleButton",
        "Toolbar",
        "TwoLineListItem",
        "VideoView",
        "ViewAnimator",
        "ViewFlipper",
        "ViewSwitcher",
        "ZoomButton",
        "ZoomControls" -> "android.widget.$name"

        "WebView" -> "android.webkit.$name"

        "FragmentBreadCrumbs",
        "MediaRouteButton" -> "android.app.$name"

        "SurfaceView",
        "TextureView",
        "View",
        "ViewStub" -> "android.view.$name"

        else -> "android.view.$name"
    }
}