package tk.stoin.ledstripremote

import android.app.Dialog
import android.app.DialogFragment
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.DialogPreference
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.onegravity.colorpicker.ColorPickerDialog
import com.onegravity.colorpicker.ColorPickerListener
import com.onegravity.colorpicker.SetColorPickerListenerEvent

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
