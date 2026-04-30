package com.example.clocking

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

class ClockingAccessibilityService : AccessibilityService() {
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        val root = rootInActiveWindow ?: return
        clickByText(root, "Do Not Show Again")
        clickByText(root, "Close")

        if (clickByText(root, "Virtual Clocking")) return

        val clickedOut = clickByText(root, "Clock out")
        if (clickedOut) {
            clickByText(root, "Clocking Reason")
            clickByText(root, "Comida")
            return
        }
        clickByText(root, "Clock in")
    }

    override fun onInterrupt() = Unit

    private fun clickByText(root: AccessibilityNodeInfo, text: String): Boolean {
        val nodes = root.findAccessibilityNodeInfosByText(text)
        for (node in nodes) {
            var n: AccessibilityNodeInfo? = node
            while (n != null) {
                if (n.isClickable) {
                    return n.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                }
                n = n.parent
            }
        }
        return false
    }
}
