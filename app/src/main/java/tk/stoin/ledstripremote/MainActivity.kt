package tk.stoin.ledstripremote

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.btnConnect)
        val loadWidget = findViewById<ProgressBar>(R.id.loadProg)
        val address = findViewById<EditText>(R.id.txtAddress)
        val errorText = (findViewById<TextView>(R.id.txtError))
        val handler = HttpHandler(this)

        button.setOnClickListener {
            button.visibility = View.INVISIBLE
            loadWidget.visibility = View.VISIBLE
            loadWidget.animate()

            handler.getControllers(address.text.toString(), {

                //on success


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
