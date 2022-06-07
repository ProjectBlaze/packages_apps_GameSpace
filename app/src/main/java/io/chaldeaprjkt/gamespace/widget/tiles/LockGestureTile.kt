/*
 * Copyright (C) 2022 CorvusOS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.chaldeaprjkt.gamespace.widget.tiles

import android.content.Context
import android.os.RemoteException
import android.os.ServiceManager
import android.util.AttributeSet
import android.util.Log
import android.view.View
import io.chaldeaprjkt.gamespace.R
import com.android.internal.statusbar.IStatusBarService
import io.chaldeaprjkt.gamespace.gamebar.GameBarService.Companion.TAG

class LockGestureTile @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : BaseTile(context, attrs) {

    private val statusBarService = IStatusBarService.Stub.asInterface(
        ServiceManager.getService(Context.STATUS_BAR_SERVICE)
    )

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        shouldLockGesture = appSettings.lockGesture
        title?.text = context.getString(R.string.lock_gesture_title)
        icon?.setImageResource(R.drawable.ic_lock_gesture)
    }

    private var shouldLockGesture: Boolean = false
        set(value) {
            field = value
            if (value) {
                summary?.text = context.getString(R.string.state_enabled)
            } else {
                summary?.text = context.getString(R.string.state_disabled)
            }
            appSettings.lockGesture = value
            isSelected = value
        }

    override fun onClick(v: View?) {
        super.onClick(v)
        shouldLockGesture = !shouldLockGesture
        try {
            statusBarService.setBlockedGesturalNavigation(shouldLockGesture)
        } catch (e: RemoteException) {
            Log.e(TAG, "Failed to toggle gesture")
        }
    }
}
