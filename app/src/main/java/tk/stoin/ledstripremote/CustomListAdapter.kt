package tk.stoin.ledstripremote

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.support.v7.app.AppCompatDialog
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.onegravity.colorpicker.ColorPickerDialog
import com.onegravity.colorpicker.ColorPickerListener
import com.onegravity.colorpicker.SetColorPickerListenerEvent
import java.util.*
import kotlin.concurrent.timer

class CustomListAdapter(val context: Context, val controllers: Array<LedControllerStatus>, val resources: Resources) : BaseExpandableListAdapter() {

    override fun getChildrenCount(p0: Int) = 1

    override fun getGroup(p0: Int): Any = controllers[p0]

    override fun getChild(p0: Int, p1: Int): Any = controllers[p0].pattern

    override fun getGroupId(p0: Int): Long = p0.toLong()

    override fun isChildSelectable(p0: Int, p1: Int): Boolean = false

    override fun hasStableIds() = false

    override fun getChildView(groupPos: Int, childPos: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup?): View? {

        val newView: View
        val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        newView = layoutInflater.inflate(R.layout.list_detail, parent, false)

        val arrayContainer = newView.findViewById<LinearLayout>(R.id.detailArrayholder)

        val colors = controllers[groupPos].pattern.colors
        for (c in colors) {
            val image = ImageView(context)
            image.setImageResource(R.drawable.led_button)
            println(c.r.toPositiveInt().toString() + ", " + c.g.toPositiveInt().toString() + ", " + c.b.toPositiveInt().toString())
            image.setColorFilter(Color.rgb(c.r.toPositiveInt(), c.g.toPositiveInt(), c.b.toPositiveInt()))
            image.setPadding(8, 0, 8, 0)
            image.setOnClickListener {
                val dialog = ColorPickerDialog(context, Color.rgb(c.r.toPositiveInt(), c.g.toPositiveInt(), c.b.toPositiveInt()), false)
                var dialogId = dialog.show()
                SetColorPickerListenerEvent.setListener(dialogId,
                        object : ColorPickerListener {

                            val task = object : TimerTask(){
                                override fun run() {
                                    shouldBeUpdated = true
                                    println("update = true")
                                }
                            }

                            init {
                                Timer().schedule(task, 0.toLong(), 500.toLong())
                            }

                            var shouldBeUpdated = false

                            override fun onColorChanged(color: Int) {
                                if (shouldBeUpdated) {
                                    c.setColor(Color.red(color).toByte(), Color.green(color).toByte(), Color.blue(color).toByte(), controllers[groupPos]::syncController, context)
                                    println("update = false")
                                    shouldBeUpdated = false
                                }
                            }

                            override fun onDialogClosing() {
                                dialogId = -1
                                task.cancel()
                            }
                        })
            }
            arrayContainer.addView(image)
        }
        val addButton = newView.findViewById<ImageView>(R.id.btnAddLed)
        addButton.setOnClickListener {
            //TODO
        }

        return newView
    }

    override fun getChildId(p0: Int, p1: Int): Long = p1.toLong()

    override fun getGroupView(p0: Int, p1: Boolean, p2: View?, p3: ViewGroup?): View? {
        val title = controllers[p0].name
        var newView = p2

        if (p2 == null) {
            val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            newView = layoutInflater.inflate(R.layout.list_header, p3, false)
        }

        if (newView != null) {
            val titleTextView = newView!!.findViewById<TextView>(R.id.txtStripName)
            titleTextView.text = title
            @Suppress("DEPRECATION")
            titleTextView.setTextColor(resources.getColor(R.color.colorText))
        }

        return newView
    }

    override fun getGroupCount() = controllers.size

    init {

    }
}