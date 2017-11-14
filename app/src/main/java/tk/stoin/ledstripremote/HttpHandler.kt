package tk.stoin.ledstripremote

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson

class HttpHandler(context: Context){

    var queue: RequestQueue

    fun getControllers(url: String, onDataReceived: (Array<LedControllerStatus>) -> Unit, onFail: () -> Unit){
        val stringRequest = StringRequest(Request.Method.GET, url,
                object : Response.Listener<String> {
                    override fun onResponse(response: String?) {
                        val controllers = Gson().fromJson(response, Array<LedControllerStatus>::class.java)
                        onDataReceived(controllers)
                    }

                }, object : Response.ErrorListener{
            override fun onErrorResponse(error: VolleyError?) {
                onFail()
            }
        })
        queue.add(stringRequest)

    }

    init {
        queue = Volley.newRequestQueue(context)
    }
}
