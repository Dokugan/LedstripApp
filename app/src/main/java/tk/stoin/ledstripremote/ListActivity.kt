package tk.stoin.ledstripremote

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.*

class ListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        val bundleObject = intent.extras
        val controllers: Array<LedControllerStatus>
        controllers = bundleObject.getSerializable("controllers") as Array<LedControllerStatus>

        val list = findViewById<ExpandableListView>(R.id.expandedList)
        list.setAdapter(CustomListAdapter(this, controllers, resources))
    }
}
