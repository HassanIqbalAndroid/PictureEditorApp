package com.photoeditor.photoeffect.multitouch

import com.example.piceditor.multitouch.PhotoView

interface OnDoubleClickListener {
    fun onPhotoViewDoubleClick(view: PhotoView, entity: MultiTouchEntity)
    fun onBackgroundDoubleClick()
}
