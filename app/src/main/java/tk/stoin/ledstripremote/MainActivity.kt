package tk.stoin.ledstripremote

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    companion object {
        var serverAddress = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.btnConnect)
        val loadWidget = findViewById<ProgressBar>(R.id.loadProg)
        val address = findViewById<EditText>(R.id.txtAddress)
        val errorText = (findViewById<TextView>(R.id.txtError))
        val handler = HttpHandler(this)

        val sharedPref = this.getSharedPreferences(getString(R.string.preference_file_key), android.content.Context.MODE_PRIVATE)
        serverAddress = sharedPref.getString(getString(R.string.address_preference_key), "")
        address.setText(serverAddress)

        button.setOnClickListener {
            button.visibility = View.INVISIBLE
            loadWidget.visibility = View.VISIBLE
            loadWidget.animate()

            if (serverAddress != address.text.toString()){
                serverAddress = address.text.toString()
                with(sharedPref.edit()){
                    putString(getString(R.string.address_preference_key), serverAddress)
                    apply()
                }
            }

            handler.getControllers({

                //on success
                val intent = Intent(this, ListActivity::class.java)
                val bundle = Bundle()
                bundle.putSerializable("controllers", it)
                intent.putExtras(bundle)
                startActivity(intent)
                loadWidget.visibility = View.INVISIBLE
                button.visibility = View.VISIBLE

            }, {
                //onFail
                loadWidget.visibility = View.INVISIBLE
                button.visibility = View.VISIBLE
                errorText.text = getString(R.string.connection_error)
            })
        }

        address.setOnClickListener{
            errorText.text = ""
        }
    }
}
