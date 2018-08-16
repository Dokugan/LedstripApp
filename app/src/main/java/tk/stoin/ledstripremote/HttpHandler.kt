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

    fun getControllers(onDataReceived: (Array<LedControllerStatus>) -> Unit, onFail: () -> Unit){
        if (!MainActivity.serverAddress.isBlank() && !MainActivity.serverAddress.isEmpty()) {
            val stringRequest = StringRequest(Request.Method.GET, MainActivity.serverAddress + "/controllers",
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
        } else onFail()
    }

    fun toggleController(controllerId: Int, state: Boolean, onSucces: () -> Unit, onFail: () -> Unit){
        if (!MainActivity.serverAddress.isBlank() && !MainActivity.serverAddress.isEmpty()){
            val stringRequest = StringRequest(Request.Method.GET, MainActivity.serverAddress + "/controller?id=$controllerId&on=${if(state){"true"}else{"false"}}",
                    object : Response.Listener<String>{
                        override fun onResponse(response: String?) {
                            onSucces()
                        }
                    }, object : Response.ErrorListener{
                override fun onErrorResponse(error: VolleyError?) {
                    onFail()
                }
            })
            queue.add(stringRequest)
        }else  onFail()
    }

    fun toggleAllControllers(state: Boolean, onSucces: () -> Unit, onFail: () -> Unit){
        if (!MainActivity.serverAddress.isBlank() && !MainActivity.serverAddress.isEmpty()){
            val stringRequest = StringRequest(Request.Method.GET, MainActivity.serverAddress + "/controllers?on=${state.toString()}",
                    object : Response.Listener<String>{
                        override fun onResponse(response: String?) {
                            onSucces()
                        }
                    }, object : Response.ErrorListener{
                override fun onErrorResponse(error: VolleyError?) {
                    onFail()
                }
            })
            queue.add(stringRequest)
        }else  onFail()
    }

    fun checkState(onSucces: (Boolean) -> Unit, onFail: () -> Unit, id: Int = -1){
        if (!MainActivity.serverAddress.isBlank() && !MainActivity.serverAddress.isEmpty()){
            if (id >= 0)
            {
                val stringRequest = StringRequest(Request.Method.GET, MainActivity.serverAddress + "/state?id=$id",
                        object : Response.Listener<String>{
                            override fun onResponse(response: String?) {
                                if (response == "true") onSucces(true)
                                else onSucces(false)
                            }
                        }, object : Response.ErrorListener{
                    override fun onErrorResponse(error: VolleyError?) {
                        onFail()
                    }
                })
                queue.add(stringRequest)
            }
            else{
                val stringRequest = StringRequest(Request.Method.GET, MainActivity.serverAddress + "/state",
                        object : Response.Listener<String>{
                            override fun onResponse(response: String?) {
                                if (response == "true") onSucces(true)
                                else onSucces(false)
                            }
                        }, object : Response.ErrorListener{
                    override fun onErrorResponse(error: VolleyError?) {
                        onFail()
                    }
                })
                queue.add(stringRequest)
            }
        }
        else onFail()
    }

    fun updateController(controller: LedControllerStatus ,onSucces: () -> Unit, onFail: () -> Unit){

        if (!MainActivity.serverAddress.isBlank() && !MainActivity.serverAddress.isEmpty()){
            val stringRequest = object : StringRequest(Request.Method.POST, MainActivity.serverAddress + "/controller?id=${controller.id}",
                    object : Response.Listener<String>{
                        override fun onResponse(response: String?) {
                            onSucces()
                        }
                    }, object : Response.ErrorListener{
                override fun onErrorResponse(error: VolleyError?) {
                    onFail()
                }
            }) {
                override fun getBody(): ByteArray {
                    val body = Gson().toJson(controller.pattern)
                    return body.toByteArray()
                }
            }
            queue.add(stringRequest)
        } else onFail()

    }

    init {
        queue = Volley.newRequestQueue(context)
    }
}
