package tk.stoin.ledstripremote

import android.content.Context
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

class ListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        val bundleObject = intent.extras
        val controllers: Array<LedControllerStatus>
        controllers = bundleObject.getSerializable("controllers") as Array<LedControllerStatus>

        val list = findViewById<ExpandableListView>(R.id.expandedList)
        list.setAdapter(
            object : BaseExpandableListAdapter() {
                override fun getChildrenCount(p0: Int) = 1

                override fun getGroup(p0: Int): Any = controllers[p0]

                override fun getChild(p0: Int, p1: Int): Any = controllers[p0].pattern

                override fun getGroupId(p0: Int): Long = p0.toLong()

                override fun isChildSelectable(p0: Int, p1: Int): Boolean = false

                override fun hasStableIds() = false

                override fun getChildView(groupPos: Int, childPos: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup?): View? {

                    var newView: View
                        val layoutInflater = applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                        newView = layoutInflater.inflate(R.layout.list_detail, parent, false)

                        val arrayContainer = newView.findViewById<LinearLayout>(R.id.detailArrayholder)

                        val colors = controllers[groupPos].pattern.colors
                        for (c in colors) {
                            val image = ImageView(applicationContext)
                            image.setImageResource(R.drawable.led_button)
                            println(c.r.toPositiveInt().toString() + ", " + c.g.toPositiveInt().toString() + ", " + c.b.toPositiveInt().toString())
                            image.setColorFilter(Color.rgb(c.r.toPositiveInt(), c.g.toPositiveInt(), c.b.toPositiveInt()))
                            image.setPadding(8, 0, 8, 0)
                            image.setOnClickListener{
                                //TODO
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

                    if(p2 == null){
                       val layoutInflater = applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                       newView = layoutInflater.inflate(R.layout.list_header, p3, false)
                    }

                    if (newView != null) {
                        val titleTextView = newView!!.findViewById<TextView>(R.id.txtStripName)
                        titleTextView.text = title
                        titleTextView.setTextColor(resources.getColor(R.color.colorText))
                    }

                    return newView
                }

                override fun getGroupCount() = controllers.size

            })
    }
}
